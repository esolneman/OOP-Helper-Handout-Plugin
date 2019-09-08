package services;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;

public class HandoutService {

    public void cloneContentBranch(){
        System.out.print("start cloning branch");

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
