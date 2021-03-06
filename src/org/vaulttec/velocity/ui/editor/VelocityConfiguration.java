package org.vaulttec.velocity.ui.editor;

import java.util.Vector;

import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;
import org.vaulttec.velocity.ui.IColorConstants;
import org.vaulttec.velocity.ui.VelocityColorProvider;
import org.vaulttec.velocity.ui.editor.text.NonRuleBasedDamagerRepairer;
import org.vaulttec.velocity.ui.editor.text.VelocityPartitionScanner;
import org.vaulttec.velocity.ui.preferences.JavaPreferenceHelper;

public class VelocityConfiguration extends SourceViewerConfiguration {

	private VelocityEditor fEditor;

	public VelocityConfiguration(VelocityEditor anEditor) {
		fEditor = anEditor;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getConfiguredContentTypes(org.eclipse.jface.text.source.ISourceViewer)
	 */
	public String[] getConfiguredContentTypes(ISourceViewer aSourceViewer) {
		return VelocityPartitionScanner.TYPES;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getTextHover(org.eclipse.jface.text.source.ISourceViewer, java.lang.String)
	 */
	public ITextHover getTextHover(ISourceViewer aSourceViewer,
									String aContentType) {
		ITextHover hover;
		if (aContentType.equals(IDocument.DEFAULT_CONTENT_TYPE) ||
				 aContentType.equals(VelocityPartitionScanner.PARSED_STRING)) {
			hover = new VelocityTextHover(fEditor);
		} else {
			hover = null;
		}
		return hover;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getAnnotationHover(org.eclipse.jface.text.source.ISourceViewer)
	 */
	public IAnnotationHover getAnnotationHover(ISourceViewer aSourceViewer) {
		return new VelocityAnnotationHover();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getContentAssistant(org.eclipse.jface.text.source.ISourceViewer)
	 */
	public IContentAssistant getContentAssistant(ISourceViewer aSourceViewer) {
		ContentAssistant assistant = new ContentAssistant();
		assistant.setContentAssistProcessor(
							  new VelocityCompletionProcessor(fEditor, true),
							  IDocument.DEFAULT_CONTENT_TYPE);
		assistant.setContentAssistProcessor(
							   new VelocityCompletionProcessor(fEditor, false),
							   VelocityPartitionScanner.PARSED_STRING);
		assistant.enableAutoInsert(true);
		assistant.enableAutoActivation(true);
		return assistant;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getDoubleClickStrategy(org.eclipse.jface.text.source.ISourceViewer, java.lang.String)
	 */
	public ITextDoubleClickStrategy getDoubleClickStrategy(
							ISourceViewer aSourceViewer, String aContentType) {
		return VelocityEditorEnvironment.getDoubleClickStrategy();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getDefaultPrefixes(org.eclipse.jface.text.source.ISourceViewer, java.lang.String)
	 */
	public String[] getDefaultPrefixes(ISourceViewer aSourceViewer,
										String aContentType) {
		return new String[] { "##", "" };
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getPresentationReconciler(org.eclipse.jface.text.source.ISourceViewer)
	 */
	public IPresentationReconciler getPresentationReconciler(
												 ISourceViewer aSourceViewer) {
		VelocityColorProvider cp = VelocityEditorEnvironment.getColorProvider();
		PresentationReconciler rec = new PresentationReconciler();

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(
								   VelocityEditorEnvironment.getCodeScanner());
		rec.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		rec.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		NonRuleBasedDamagerRepairer ndr = new NonRuleBasedDamagerRepairer(
					  new TextAttribute(cp.getColor(IColorConstants.COMMENT)));
		rec.setDamager(ndr, VelocityPartitionScanner.SINGLE_LINE_COMMENT);
		rec.setRepairer(ndr, VelocityPartitionScanner.SINGLE_LINE_COMMENT);

		ndr = new NonRuleBasedDamagerRepairer(new TextAttribute(
										cp.getColor(IColorConstants.COMMENT)));
		rec.setDamager(ndr, VelocityPartitionScanner.MULTI_LINE_COMMENT);
		rec.setRepairer(ndr, VelocityPartitionScanner.MULTI_LINE_COMMENT);

		ndr = new NonRuleBasedDamagerRepairer(new TextAttribute(
									cp.getColor(IColorConstants.DOC_COMMENT)));
		rec.setDamager(ndr, VelocityPartitionScanner.DOC_COMMENT);
		rec.setRepairer(ndr, VelocityPartitionScanner.DOC_COMMENT);

		ndr = new NonRuleBasedDamagerRepairer(new TextAttribute(
										 cp.getColor(IColorConstants.STRING)));
		rec.setDamager(ndr, VelocityPartitionScanner.UNPARSED_STRING);
		rec.setRepairer(ndr, VelocityPartitionScanner.UNPARSED_STRING);

		dr = new DefaultDamagerRepairer(
								 VelocityEditorEnvironment.getStringScanner());
		rec.setDamager(dr, VelocityPartitionScanner.PARSED_STRING);
		rec.setRepairer(dr, VelocityPartitionScanner.PARSED_STRING);
		return rec;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getReconciler(org.eclipse.jface.text.source.ISourceViewer)
	 */
	public IReconciler getReconciler(ISourceViewer aSourceViewer) {
		return new MonoReconciler(fEditor.getReconcilingStrategy(), false);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getIndentPrefixes(org.eclipse.jface.text.source.ISourceViewer, java.lang.String)
	 */
	public String[] getIndentPrefixes(ISourceViewer aSourceViewer,	
									  String aContentType) {
		Vector<String> prefixes = new Vector<String>();

		// Create prefixes from JDT preferences settings
		int tabWidth = getTabWidth(aSourceViewer);
		IPreferenceStore prefs = JavaPreferenceHelper.getJavaPreferneceStore();
		boolean useSpaces = prefs.getBoolean(
				DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR);
		// prefix[0] is either '\t' or ' ' x tabWidth, depending on useSpaces
		for (int i = 0; i <= tabWidth; i++) {
		    StringBuffer prefix = new StringBuffer();
			if (useSpaces) {
			    for (int j = 0; j + i < tabWidth; j++) {
			    	prefix.append(' ');
			    }		    	
				if (i != 0) {
		    		prefix.append('\t');				
				}
			} else {    
			    for (int j= 0; j < i; j++) {
			    	prefix.append(' ');
			    }
				if (i != tabWidth) {
		    		prefix.append('\t');
				}
			}
			prefixes.add(prefix.toString());
		}
		prefixes.add("");
		return (String[])prefixes.toArray(new String[prefixes.size()]);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getTabWidth(org.eclipse.jface.text.source.ISourceViewer)
	 */
	public int getTabWidth(ISourceViewer aSourceViewer) {

		// Get tab width from JDT preferences settings
//		IPreferenceStore prefs = JavaPreferenceHelper.getJavaPreferneceStore();
//		return prefs.getInt(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_TAB_WIDTH);
		return 4;
	}
}
