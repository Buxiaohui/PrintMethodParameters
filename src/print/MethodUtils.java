package print;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;

public class MethodUtils {
    public static String getParametersLogStr(PsiMethod method, Project project) {
        if (method != null) {
            StringBuffer stringBuffer = null;
            PsiParameterList psiParameterList = method.getParameterList();
            PsiParameter[] psiParameters = psiParameterList.getParameters();
            if (psiParameters.length <= 0) {
                return "if (LogUtil.LOGGABLE) {\n" +
                        "   LogUtil.e(TAG,  \"" + method.getName() + "\");\n" +
                        "}";
            } else {
                stringBuffer = new StringBuffer();
                stringBuffer.append("if (LogUtil.LOGGABLE) {\n");
                stringBuffer.append("LogUtil.e(TAG,  \"" + method.getName() + "-");
                for (int i = 0; i < psiParameters.length; i++) {
                    PsiParameter p = psiParameters[i];
                    if (i == 0) {
                        stringBuffer.append(p.getName() + ":\"+");
                    } else {
                        stringBuffer.append("+\"," + p.getName() + ":\"+");
                    }
                    stringBuffer.append(p.getName());

                }
                stringBuffer.append(");\n");
                stringBuffer.append("}");
                // Messages.showInputDialog(project, stringBuffer.toString(), "Input your name", Messages.getQuestionIcon());
                return stringBuffer.toString();
            }
        }
        return "";
    }
}
