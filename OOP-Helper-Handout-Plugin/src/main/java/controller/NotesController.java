package controller;

import objects.Notes;
import provider.LocalStorageDataProvider;
import provider.RepoLocalStorageDataProvider;
import provider.contentHandler.ParseNotesJson;

import java.io.File;

public class NotesController {


    public static void createNotesFile(){

    }

    public static void saveNewEntryInFile(String htmlText){
        System.out.println("HTMLTEXT: " + htmlText);
        //Notes.Note newNote = ParseNotesJson.getNoteFromString(htmlText);
        File notesFile = LocalStorageDataProvider.getNotesFile();
    }

}
