package org.vaulttec.velocity.ui.editor;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.velocity.runtime.RuntimeInstance;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.Token;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IFileEditorInput;
import org.vaulttec.velocity.ui.editor.parser.NodeVisitor;
import org.vaulttec.velocity.ui.model.ITreeNode;
import org.vaulttec.velocity.ui.model.Template;

import com.langtags.ep4velo.Activator;

/**
 * Reconciler strategy which parses the whole editor's content (a Velocity
 * template) on a document change.
 */
public class VelocityReconcilingStrategy implements IReconcilingStrategy {
	private VelocityEditor fEditor;
    private Template fTemplate;
    private Template fLastTemplate;
	private String fError;

	public VelocityReconcilingStrategy(VelocityEditor anEditor) {
		fEditor = anEditor;
	}

	public void setDocument(IDocument aDocument) {
		parse();
	}

	public void reconcile(DirtyRegion aDirtyRegion, IRegion aRegion) {
		parse();
	}

	public void reconcile(IRegion aPartition) {
		parse();
	}

	private void parse() {
		IFile file = ((IFileEditorInput)fEditor.getEditorInput()).getFile(); 
		String name = file.getName();
		Reader reader = new StringReader(fEditor.getDocument().get());
		Template template = null;
		try {
			file.deleteMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
			RuntimeInstance runtime =
								 VelocityEditorEnvironment.getParser();
			SimpleNode root = runtime.parse(reader, name);

			// Create tree model			
			NodeVisitor visitor = new NodeVisitor(name);
			root.jjtAccept(visitor, null);
			template = visitor.getTemplate();
			fError = "";
		} catch (ParseException e) {
			if (e.getMessage() != null) {
				fError = e.getMessage();
				Token token = e.currentToken;
				if (token != null) {
					fEditor.addProblemMarker(e.getMessage(),
											 token.next.beginLine);
				}
			} else {
				fError = "";
			}
		} catch (Exception e) {
			fError = "";
			Activator.log(e);
        } finally {
        	try {
				reader.close();        
        	} catch (IOException e) {
        		Activator.log(e);
        	}
		}

		// Replace saved template with the new parsed one
		synchronized (this) {
        	if (template != null) {
				fTemplate = template;

				// Save last successful parse tree
				fLastTemplate = template;
        	} else {
        		fTemplate = null;
        	}
		}

		// Update outline view and display error message in status line
		Display.getDefault().syncExec(new Runnable() {
			public void run(){	
				fEditor.updateOutlinePage();
				fEditor.displayErrorMessage(fError);
			}
		});
	}

	/**
	 * Returns root elements of current parse tree.
	 */    
    public Object[] getRootElements() {
		return (fTemplate != null ? fTemplate.getChildren() :
									 ITreeNode.NO_CHILDREN);
    }

	/**
	 * Returns root node of current parse tree.
	 */    
    public ITreeNode getRootNode() {
        return fTemplate;
    }

	/**
	 * Returns last successful parse tree.
	 */    
    public ITreeNode getLastRootNode() {
        return fLastTemplate;
    }
}
