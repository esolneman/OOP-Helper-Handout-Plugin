package environment;

import com.intellij.openapi.project.Project;

public class ContentDataPaths {
    private static String projectDirectory;

    public void setUserProjectDirectory (Project project){
        projectDirectory = project.getBasePath();
    }

    public String getHandoutContentPath () {
        return projectDirectory + "";
    }
}
