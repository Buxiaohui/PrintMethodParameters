package print;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiMethod;
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
        return MethodUtils.getParametersLogStr(method, project);
    }


}
