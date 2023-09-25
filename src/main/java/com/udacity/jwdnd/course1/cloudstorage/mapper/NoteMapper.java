package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Select("SELECT * FROM NOTES WHERE noteid = #{noteId}")
    Note getNoteById(Integer noteId);

    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    List<Note> getNotesByUserId(Integer userId);

    @Insert("INSERT INTO NOTES (" +
            "notetitle, " +
            "notedescription, " +
            "userid) VALUES (" +
            "#{note.noteTitle}, " +
            "#{note.noteDescription}, " +
            "#{note.userId})")
    @Options(useGeneratedKeys = true, keyProperty = "note.noteId")
    int addNote(@Param("note") Note note);

    @Update("UPDATE NOTES SET " +
            "notetitle = #{newNote.noteTitle}, " +
            "notedescription = #{newNote.noteDescription} " +
            "WHERE noteid = #{noteId}")
    int updateNoteById(@Param("newNote") Note newNote, @Param("noteId") int noteId);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteId}")
    int deleteNoteById(@Param("noteId") Integer noteId);
}
