package services.impl;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import services.ContentService;
import com.intellij.openapi.project.Project;

import java.io.File;
import java.util.Arrays;

public class ContentServiceImpl implements ContentService {
    public ContentServiceImpl(Project project) {
    }

    public void cloneBranch(){
    System.out.println("start cloning branch");

        String repoUrl = "https://github.com/esolneman/OOP-Helper-Handout-Template.git";
        String cloneDirectoryPath = "refs/heads/test";
        String fileName = "/RepoTEST";

        Git git;
        {
            try {
                git = Git.cloneRepository()
                        .setURI(repoUrl)
                        .setDirectory(new File(fileName))
                        .setBranchesToClone(Arrays.asList(cloneDirectoryPath))
                        .setBranch(cloneDirectoryPath)
                        .call();
            } catch (GitAPIException e) {
                System.out.print(e);
            }
        }
    }
}
