package com.udacity.jwdnd.course1.cloudstorage.service.impl;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.service.INoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService implements INoteService {
    private final NoteMapper noteMapper;
    private final UserService userService;

    public List<Note> getNotesByUserId(int userId) {
        return noteMapper.getNotesByUserId(userId);
    }

    public int createNote(Note note) {
        return noteMapper.addNote(new Note(
                null, note.getNoteTitle(), note.getNoteDescription(), note.getUserId()
        ));
    }

    public int deleteNoteById(int noteId) {
        return noteMapper.deleteNoteById(noteId);
    }

    public int updateNote(Note note, int noteId) {
        Note existingNote = noteMapper.getNoteById(note.getNoteId());
        existingNote.setNoteDescription(note.getNoteDescription());
        existingNote.setNoteTitle(note.getNoteTitle());
        return noteMapper.updateNoteById(existingNote, noteId);
    }

    public Note findNoteById(int id) {
        return noteMapper.getNoteById(id);
    }

}