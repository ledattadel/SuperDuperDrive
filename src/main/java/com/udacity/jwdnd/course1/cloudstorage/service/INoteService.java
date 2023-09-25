package com.udacity.jwdnd.course1.cloudstorage.service;

import java.util.List;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;

public interface INoteService {
    List<Note> getNotesByUserId(int userId);

    int createNote(Note note);

    int deleteNoteById(int noteId);

    int updateNote(Note note, int noteId);

    Note findNoteById(int id);
}
