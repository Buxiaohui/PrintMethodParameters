package print;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import org.apache.http.util.TextUtils;

public class MethodBean {
    private String name;
    private String parameters;
    private String returnObj;
    private PsiMethod method;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParamters(String parameters) {
        this.parameters = parameters;
    }

    public String getReturnObj() {
        return returnObj;
    }

    public void setReturnObj(String returnObj) {
        this.returnObj = returnObj;
    }

    public PsiMethod getMethod() {
        return method;
    }

    public void setMethod(PsiMethod method) {
        this.method = method;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append(name);
        sb.append("(");
        sb.append(parameters == null ? "" : parameters);
        sb.append(")");
        if (!TextUtils.isEmpty(returnObj)) {
            sb.append(" :");
            sb.append(returnObj);
        }
        return sb.toString();
    }

    public String getParametersLogStr(Project project) {
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
