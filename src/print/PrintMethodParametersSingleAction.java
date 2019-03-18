package print;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.JBPopupAdapter;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.LightweightWindowEvent;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.components.JBList;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PrintMethodParametersSingleAction extends AnAction {

    public void actionPerformed(AnActionEvent event) {

        Project project = event.getData(PlatformDataKeys.PROJECT);
        //获取当前编辑器
        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        int caret = editor.getCaretModel().getOffset();
        PsiFile file = event.getData(LangDataKeys.PSI_FILE);
        PsiMethod method = PsiTreeUtil.getParentOfType(file.findElementAt(caret), PsiMethod.class);

        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);
        if (method != null) {
            WriteCommandAction.runWriteCommandAction(project, new Runnable() {
                @Override
                public void run() {
                    PsiStatement logPsiCodeBlock = elementFactory.createStatementFromText(MethodUtils.getParametersLogStr(method, project), method);
                    PsiJavaToken psiJavaToken = method.getBody().getLBrace();
                    method.addAfter(logPsiCodeBlock, psiJavaToken);
                }
            });
        }
    }
}
