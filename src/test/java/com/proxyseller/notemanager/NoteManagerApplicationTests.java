package com.proxyseller.notemanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.proxyseller.notemanager.controllers.NoteController;
import com.proxyseller.notemanager.database.entities.NoteEntity;
import com.proxyseller.notemanager.database.services.NoteEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.Assert;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class NoteManagerApplicationTests {

	String testLines = "Hello World";
	ObjectMapper mapper;

	NoteEntity testNote;

	@Autowired
	ApplicationContext context;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void setUp(){
		NoteEntityRepository noteEntityRepository = context.getBean(NoteEntityRepository.class);
		testNote = new NoteEntity(testLines);
		noteEntityRepository.insert(testNote);

		mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	}

	@Test
	void contextLoads() {
		Assert.notNull(context.getBean(NoteController.class), "Note controller not found");
	}

	@Test
	@WithMockUser
	public void getNotes() throws Exception {
		mockMvc.perform(get("/notes"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"));
	}

	@Test
	public void getNotes_noAuth() throws Exception {
		mockMvc.perform(get("/notes"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"));
	}

	@Test
	@WithMockUser
	public void putNote_withAuth() throws Exception {
		putNote();
	}

	@Test
	public void putNote_noAuth() throws Exception {
		putNote();
	}

	@Test
	@WithMockUser
	public void likeNote() throws Exception {
		mockMvc.perform(post("/notes/like/{0}", testNote.id))
				.andDo(print())
				.andExpect(status().isNoContent());

		checkNoteLikes(testNote.id,1);
	}

	@Test
	public void likeNote_noAuth() throws Exception {
		mockMvc.perform(post("/notes/like/{0}", testNote.id))
				.andDo(print())
				.andExpect(status().isUnauthorized());

		checkNoteLikes(testNote.id,0);
	}

	@Test
	@WithMockUser
	public void unlikeNote() throws Exception {
		mockMvc.perform(post("/notes/like/{0}", testNote.id))
				.andDo(print())
				.andExpect(status().isNoContent());

		checkNoteLikes(testNote.id,1);

		mockMvc.perform(delete("/notes/like/{0}", testNote.id))
				.andDo(print())
				.andExpect(status().isNoContent());

		checkNoteLikes(testNote.id,0);
	}

	@Test
	public void unlikeNote_noAuth() throws Exception {
		mockMvc.perform(post("/notes/like/{0}", testNote.id))
				.andDo(print())
				.andExpect(status().isUnauthorized());
	}

	void checkNoteLikes(String noteId, long numOfLikes) throws Exception {
		mockMvc.perform(get("/notes/{0}", noteId))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers
						.jsonPath("$.likes").value(numOfLikes));
	}

	void putNote() throws Exception {
		String testLine = "new test line";
		String requestJson = mapper.writeValueAsString(new NoteEntity(testLine));

		mockMvc.perform(post("/notes")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(requestJson))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers
						.jsonPath("$.likes").value(0))
				.andExpect(MockMvcResultMatchers
						.jsonPath("$.lines").value(testLine));
	}
}
