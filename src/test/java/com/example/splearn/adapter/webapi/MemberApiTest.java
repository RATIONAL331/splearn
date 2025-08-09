package com.example.splearn.adapter.webapi;

import com.example.splearn.application.member.provided.MemberRegister;
import com.example.splearn.domain.member.Member;
import com.example.splearn.domain.member.MemberFixture;
import com.example.splearn.domain.member.MemberRegisterRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(MemberApi.class)
class MemberApiTest {
	@MockitoBean
	private MemberRegister memberRegister;

	@Autowired
	private MockMvcTester mvcTester;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void registerTest() throws JsonProcessingException {
		MemberRegisterRequest req = MemberFixture.createMemberRegisterRequest("test@test.com");
		Member member = Member.register(req, MemberFixture.createPasswordEncoder());
		ReflectionTestUtils.setField(member, "id", 1L);
		when(memberRegister.register(any())).thenReturn(member);

		assertThat(mvcTester.post()
		                    .uri("/api/members")
		                    .contentType(MediaType.APPLICATION_JSON)
		                    .content(objectMapper.writeValueAsBytes(req)))
				.hasStatusOk()
				.bodyJson()
				.extractingPath("$.memberId")
				.asNumber()
				.isEqualTo(1);

		verify(memberRegister).register(req);
	}

	@Test
	void registerFailTest() throws JsonProcessingException {
		MemberRegisterRequest req = MemberFixture.createMemberRegisterRequest("1234");

		assertThat(mvcTester.post()
		                    .uri("/api/members")
		                    .contentType(MediaType.APPLICATION_JSON)
		                    .content(objectMapper.writeValueAsBytes(req)))
				.hasStatus4xxClientError();

		verify(memberRegister, never()).register(req);
	}

}