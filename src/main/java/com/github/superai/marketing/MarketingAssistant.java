

package com.github.superai.marketing;

import com.github.superai.service.LoggingAdvisor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Slf4j
@Service
public class MarketingAssistant {

	private final ChatClient chatClient;

	@Value("classpath:/prompts/ideation.st")
	private Resource ideationPrompt;

	@Value("classpath:/prompts/calendar.st")
	private Resource calendarPrompt;

	public MarketingAssistant(ChatClient.Builder modelBuilder, ChatMemory chatMemory) {

		// @formatter:off
		this.chatClient = modelBuilder
				.defaultAdvisors(
						new PromptChatMemoryAdvisor(chatMemory), // Chat Memory
						new LoggingAdvisor())

				.build();
		// @formatter:on
	}



	public List<ArticleCalendarEntry> getCalendar(String chatId, GenerateCalendarRequest generateCalendarRequest) {
		log.info("Calling calendar service");

		BeanOutputConverter<List<ArticleCalendarEntry>> converter = new BeanOutputConverter<>(
				new ParameterizedTypeReference<List<ArticleCalendarEntry>>() { });

		String format = converter.getFormat();


		PromptTemplate pt = new PromptTemplate(calendarPrompt);
		Prompt renderedPrompt = pt.create(
				Map.of("today", LocalDate.now(),
						"start_date", generateCalendarRequest.startDate(),
						"articles", generateCalendarRequest.articles(),
						"format", format));

		String template =
				renderedPrompt.getContents();

		log.info("Template - {}", template);


		String content =
				this.chatClient.prompt(renderedPrompt)
						.call().chatResponse().getResult().getOutput().getContent();

		return converter.convert(content);
	}

	public List<String> getIdeas(String chatId, GenerateIdeaRequest generateIdeaRequest) {
		log.info("Calling v2 service");

		ListOutputConverter converter = new ListOutputConverter(new DefaultConversionService());
		String format = converter.getFormat();


		PromptTemplate pt = new PromptTemplate(ideationPrompt);
		Prompt renderedPrompt = pt.create(
				Map.of("topic_details", generateIdeaRequest.topic(),
						"goals_intentions", generateIdeaRequest.goal(),
						"format", format));

		String template =
		renderedPrompt.getContents();

		log.info("Template - {}", template);


		String content =
		this.chatClient.prompt(renderedPrompt)
				.call().chatResponse().getResult().getOutput().getContent();

		return converter.convert(content);
	}
}