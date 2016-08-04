package org.vaulttec.velocity.ui.editor;

import org.eclipse.jdt.ui.actions.IJavaEditorActionDefinitionIds;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.ide.IDEActionFactory;
import org.eclipse.ui.part.EditorActionBarContributor;
import org.eclipse.ui.texteditor.BasicTextEditorActionContributor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.RetargetTextEditorAction;
import org.vaulttec.velocity.ui.editor.actions.IVelocityActionConstants;
import org.vaulttec.velocity.ui.editor.actions.IVelocityActionDefinitionIds;
import org.vaulttec.velocity.ui.editor.actions.TogglePresentationAction;

import com.langtags.ep4velo.Activator;

/**
 * Contributes Velocity actions to the desktop's edit menu and registers the
 * according global action handlers.
 */
public class VelocityEditorActionContributor
									extends BasicTextEditorActionContributor {
	private static final String PREFIX = "VelocityEditor.";
	private TogglePresentationAction fTogglePresentation;
	private RetargetTextEditorAction fGotoDefinition;
	private RetargetTextEditorAction fContentAssist;
	private RetargetTextEditorAction fComment;
	private RetargetTextEditorAction fUncomment;

	/**
	 * Defines the menu actions and their action handlers.
	 */
	public VelocityEditorActionContributor() {
		createActions();
	}

	protected void createActions() {

		// Define toolbar actions
		fTogglePresentation = new TogglePresentationAction();

		// Define text editor actions
		fGotoDefinition = new RetargetTextEditorAction(
				Activator.getResourceBundle(),
								PREFIX + "GotoDefinition.");
		fGotoDefinition.setActionDefinitionId(
								  IVelocityActionDefinitionIds.GOTO_DEFINITION);
		fContentAssist = new RetargetTextEditorAction(
				Activator.getResourceBundle(),
								PREFIX + "ContentAssist.");
		fContentAssist.setActionDefinitionId(
					   ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
		fComment = new RetargetTextEditorAction(
				Activator.getResourceBundle(),
								PREFIX + "Comment.");
		fComment.setActionDefinitionId(IJavaEditorActionDefinitionIds.COMMENT);
		fUncomment = new RetargetTextEditorAction(
				Activator.getResourceBundle(),
								PREFIX + "Uncomment.");
		fUncomment.setActionDefinitionId(
									  IJavaEditorActionDefinitionIds.UNCOMMENT);
	}

	/* (non-Javadoc)
	 * @see IEditorActionBarContributor#setActiveEditor(IEditorPart)
	 */
	public void setActiveEditor(IEditorPart aPart) {
		super.setActiveEditor(aPart);
		doSetActiveEditor(aPart);
	}
	
	/**
	 * Sets the active editor to the actions provided by this contributor.
	 * @param aPart the editor
	 */
	private void doSetActiveEditor(IEditorPart aPart) {
		IStatusLineManager manager = getActionBars().getStatusLineManager();
		manager.setMessage(null);
		manager.setErrorMessage(null);

		// Set the underlying action (registered by the according editor) in
		// the action handlers
		ITextEditor editor = null;
		if (aPart instanceof ITextEditor) {
			editor = (ITextEditor)aPart;
		}
		fTogglePresentation.setEditor(editor);		
		fGotoDefinition.setAction(getAction(editor,
									IVelocityActionConstants.GOTO_DEFINITION));
		fContentAssist.setAction(getAction(editor,
									 IVelocityActionConstants.CONTENT_ASSIST));
		fComment.setAction(getAction(editor,
									 IVelocityActionConstants.COMMENT));
		fUncomment.setAction(getAction(editor,
									   IVelocityActionConstants.UNCOMMENT));
		// Set global action handlers according to the given editor
		IActionBars actionBars = getActionBars();
		if (actionBars != null) {
			actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(),
				getAction(editor, ITextEditorActionConstants.DELETE));
			actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(),
				getAction(editor, ITextEditorActionConstants.UNDO));
			actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(),
				getAction(editor, ITextEditorActionConstants.REDO));
			actionBars.setGlobalActionHandler(ActionFactory.CUT.getId(),
				getAction(editor, ITextEditorActionConstants.CUT));
			actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(),
				getAction(editor, ITextEditorActionConstants.COPY));
			actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(),
				getAction(editor, ITextEditorActionConstants.PASTE));
			actionBars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(),
				getAction(editor, ITextEditorActionConstants.SELECT_ALL));
			actionBars.setGlobalActionHandler(ActionFactory.FIND.getId(),
				getAction(editor, ITextEditorActionConstants.FIND));
			actionBars.setGlobalActionHandler(IDEActionFactory.BOOKMARK.getId(),
				getAction(editor, IDEActionFactory.BOOKMARK.getId()));
			actionBars.setGlobalActionHandler(IDEActionFactory.ADD_TASK.getId(),
				getAction(editor, IDEActionFactory.ADD_TASK.getId()));

			actionBars.setGlobalActionHandler(ITextEditorActionConstants.GOTO_LINE,
				getAction(editor, ITextEditorActionDefinitionIds.LINE_GOTO));

			actionBars.setGlobalActionHandler(IJavaEditorActionDefinitionIds.COMMENT,
				getAction(editor, IVelocityActionConstants.COMMENT));
			actionBars.setGlobalActionHandler(IJavaEditorActionDefinitionIds.UNCOMMENT,
				getAction(editor, IVelocityActionConstants.UNCOMMENT));

			actionBars.setGlobalActionHandler(IVelocityActionConstants.GOTO_DEFINITION,
				getAction(editor, IVelocityActionConstants.GOTO_DEFINITION));

			actionBars.updateActionBars();
		}
	}
	
	/**
	 * @see EditorActionBarContributor#contributeToMenu(IMenuManager)
	 */
	public void contributeToMenu(IMenuManager aMenuManager) {
		super.contributeToMenu(aMenuManager);

		// Add actions to desktop's edit menu
		IMenuManager menu = aMenuManager.findMenuUsingPath(
											 IWorkbenchActionConstants.M_EDIT);
		if (menu != null) {
			menu.add(fContentAssist);
			menu.add(fComment);
			menu.add(fUncomment);
		}

		// Add actions to desktop's navigate menu
		menu = aMenuManager.findMenuUsingPath(
										 IWorkbenchActionConstants.M_NAVIGATE);
		if (menu != null) {
			menu.appendToGroup(IWorkbenchActionConstants.MB_ADDITIONS,
							   fGotoDefinition);
		}
	}

	/**
	 * @see org.eclipse.ui.part.EditorActionBarContributor#contributeToToolBar(org.eclipse.jface.action.IToolBarManager)
	 */
	public void contributeToToolBar(IToolBarManager aToolBar) {
		aToolBar.add(new Separator());
		aToolBar.add(fTogglePresentation);		
	}

	/**
	 * @see org.eclipse.ui.IEditorActionBarContributor#dispose()
	 */
	public void dispose() {
		doSetActiveEditor(null);
		super.dispose();
	}
}
