package services.impl;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import services.ContentService;
import com.intellij.openapi.project.Project;

import java.io.File;
import java.util.Arrays;

public class ContentServiceImpl implements ContentService {

    String repoUrl = "https://github.com/esolneman/OOP-Helper-Handout-Template.git";
    String CLONE_DIRECTORY_PATH = "refs/heads/test";
    String CONTENT_FILE_NAME = "/RepoTEST";
    String projectDirectory;
    String contentRepoPath;

    public ContentServiceImpl(Project project) {
        projectDirectory = project.getBasePath();
        //ModuleRootManager.getInstance(ModuleManager.getInstance(project).getModules()[0]).getContentRoots()[0]
        contentRepoPath = projectDirectory + CONTENT_FILE_NAME;
        System.out.println(contentRepoPath);

    }

    public void cloneBranch(){
        System.out.println("start cloning branch");

        //https://www.codeaffine.com/2014/12/09/jgit-authentication/
       /* CloneCommand cloneCommand = Git.cloneRepository();
        cloneCommand.setURI( "https://example.com/repo.git" );
        cloneCommand.setCredentialsProvider( new UsernamePasswordCredentialsProvider( "", "" ) );
       */

        //https://www.vogella.com/tutorials/JGit/article.html#example-for-using-jgit
        Git git;

        {
            try {
                git = Git.cloneRepository()
                        .setURI(repoUrl)
                        .setDirectory(new File(contentRepoPath))
                        .setBranchesToClone(Arrays.asList(CLONE_DIRECTORY_PATH))
                        .setBranch(CLONE_DIRECTORY_PATH)
                        .call();
            } catch (GitAPIException e) {
                System.out.print(e);
            }
        }
    }

    public void updateBranch(){}
    private void getLocalRepository(){
/*        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        Repository repository = repositoryBuilder.setGitDir(new File("/path/to/repo/.git"))
                .readEnvironment() // scan environment GIT_* variables
                .findGitDir() // scan up the file system tree
                .setMustExist(true)
                .build();*/
    }
}
