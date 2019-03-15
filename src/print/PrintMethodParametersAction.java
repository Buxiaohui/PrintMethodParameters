package print;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupAdapter;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.LightweightWindowEvent;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.components.JBList;
import com.intellij.ui.content.ContentManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PrintMethodParametersAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        //获取当前工程对象
        Project project = event.getData(PlatformDataKeys.PROJECT);

        //获取当前编辑器
        Editor editor = event.getData(PlatformDataKeys.EDITOR);

        PsiElement psiElement = event.getData(PlatformDataKeys.PSI_ELEMENT);

        ContentManager contentManager = event.getData(PlatformDataKeys.CONTENT_MANAGER);

        //获取选择对象
        SelectionModel selectionModel = editor.getSelectionModel();


        // 当前正在编辑的文件
        PsiFile psiFile = event.getData(LangDataKeys.PSI_FILE);
        PsiElement element = psiFile.findElementAt(editor.getCaretModel().getOffset());
        PsiClass psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
        String classSimpleName = psiClass.getClass().getSimpleName();
        String className = psiClass.getClass().getName();
        String name = psiClass.getName();
        PsiIdentifier nameIdentifier = psiClass.getNameIdentifier();
        String nameIdentifierName = nameIdentifier.getText();
        PsiMethod[] allMethods = psiClass.getAllMethods();
        PsiMethod[] methods = psiClass.getMethods();
        MethodBean[] methodBeans = new MethodBean[methods.length];
        JList<MethodBean> methodList = new JBList<>();
        for (int j = 0; j < methods.length; j++) {
            PsiMethod method = methods[j];
            MethodBean methodBean = new MethodBean();
            methodBean.setMethod(method);
            methodBean.setName(method.getName());
            PsiParameterList psiParameterList = method.getParameterList();
            StringBuffer parametersStr = new StringBuffer();
            for (int i = 0; i < psiParameterList.getParametersCount(); i++) {
                parametersStr.append(psiParameterList.getParameters()[i].getText());
            }
            methodBean.setParamters(parametersStr.toString());
            PsiTypeElement psiTypeElement = method.getReturnTypeElement();
            if (psiTypeElement != null) {
                PsiType psiType = psiTypeElement.getType();
                if (psiType != null) {
                    methodBean.setReturnObj(psiType.getPresentableText() + "");
                }
            }
            //  methodBean.getParametersLogStr(project); // fot test
            methodBeans[j] = methodBean;
        }
        // TODO UI 太紧凑了
        methodList.setLayout(new BorderLayout(10, 10));
        methodList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        methodList.setListData(methodBeans);

        PopupChooserBuilder popupChooserBuilder = JBPopupFactory.getInstance().createListPopupBuilder(methodList);
        popupChooserBuilder.addListener(new JBPopupAdapter() {
            @Override
            public void beforeShown(LightweightWindowEvent event) {
                super.beforeShown(event);
            }

            @Override
            public void onClosed(LightweightWindowEvent event) {
                super.onClosed(event);
                List<MethodBean> selectValueList = methodList.getSelectedValuesList();
                for (int i = 0; i < selectValueList.size(); i++) {
                    MethodBean methodBean = selectValueList.get(i);
                    PsiMethod method = methodBean.getMethod();
                    // 通过获取到PsiElementFactory来创建相应的Element，包括字段，方法，注解，类，内部类等等
                    PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);

                    WriteCommandAction.runWriteCommandAction(project, new Runnable() {
                        @Override
                        public void run() {
                            PsiStatement logPsiCodeBlock = elementFactory.createStatementFromText(methodBean.getParametersLogStr(project), method);
                            PsiJavaToken psiJavaToken = method.getBody().getLBrace();
                            method.addAfter(logPsiCodeBlock, psiJavaToken);
                        }
                    });

                }
            }
        });
        popupChooserBuilder.setTitle("Chose method to print parameters");
        popupChooserBuilder.setItemChoosenCallback(new Runnable() {
            @Override
            public void run() {

            }
        });
        popupChooserBuilder.createPopup().showInFocusCenter();

        //获取选中的值
        String text = selectionModel.getSelectedText();
        System.out.println(text);
        //Messages.showMessageDialog(project, text, "Information", Messages.getInformationIcon());

    }
}
