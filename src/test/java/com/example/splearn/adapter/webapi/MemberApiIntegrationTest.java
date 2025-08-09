package com.example.splearn.adapter.webapi;

import com.example.splearn.adapter.webapi.dto.MemberRegisterResponse;
import com.example.splearn.application.member.provided.MemberRegister;
import com.example.splearn.application.member.required.MemberRepository;
import com.example.splearn.domain.member.Member;
import com.example.splearn.domain.member.MemberFixture;
import com.example.splearn.domain.member.MemberRegisterRequest;
import com.example.splearn.domain.member.MemberStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MemberApiIntegrationTest {
	@Autowired
	private MockMvcTester mvcTester;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private MemberRegister memberRegister;

	@Test
	void registerTest() throws JsonProcessingException, UnsupportedEncodingException {
		MemberRegisterRequest req = MemberFixture.createMemberRegisterRequest("test@test.com");

		MvcTestResult result = mvcTester.post()
		                                .uri("/api/members")
		                                .contentType(MediaType.APPLICATION_JSON)
		                                .content(objectMapper.writeValueAsBytes(req))
		                                .exchange();

		assertThat(result)
				.hasStatusOk()
				.bodyJson()
				.hasPathSatisfying("$.memberId", value -> assertThat(value).isNotNull())
				.hasPathSatisfying("$.email", value -> assertThat(value).isEqualTo(req.email()));

		MemberRegisterResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), MemberRegisterResponse.class);
		Member member = memberRepository.findById(response.memberId()).orElseThrow();

		assertThat(member.getEmail().address()).isEqualTo(req.email());
		assertThat(member.isActive()).isFalse();
		assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
		assertThat(member.getNickname()).isEqualTo(req.nickname());
		assertThat(member.getPassword()).isNotNull();
	}

	@Test
	void duplicateEmailTest() throws JsonProcessingException {
		MemberRegisterRequest req = MemberFixture.createMemberRegisterRequest("test@test.com");
		memberRegister.register(req); // already registered

		MvcTestResult result = mvcTester.post()
		                                .uri("/api/members")
		                                .contentType(MediaType.APPLICATION_JSON)
		                                .content(objectMapper.writeValueAsBytes(req))
		                                .exchange();

		assertThat(result)
				.apply(print())
				.hasStatus4xxClientError();
	}

}