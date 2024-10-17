package com.github.superai.marketing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/marketing")
public class MarketingController {

    private final MarketingAssistant marketingAssistant;
    @GetMapping
    public String viewMarketing(Model model) {
        return "marketing";
    }

    @GetMapping("/ideation")
    public String viewIdeationForm(Model model) {
        return "ideation";
    }

    @PostMapping("/ideation")
    public String generateIdeas(
            GenerateIdeaRequest generateIdeaRequest,
            Model model) {

        log.info("Generating ideas - {}", generateIdeaRequest);

        String chatId = UUID.randomUUID().toString();

        model.addAttribute("chatId", chatId);

        List<String> ideas = marketingAssistant.getIdeas(chatId, generateIdeaRequest);

        log.info("Generated ideas - {}", ideas);

        model.addAttribute("ideas", ideas);

        return "ideas :: #ideas";
    }


    @GetMapping("/schedule")
    public String viewScheduleForm(Model model) {
        return "schedule";
    }

    @PostMapping("/schedule")
    public String generateSchedule(
            GenerateCalendarRequest generateCalendarRequest,
            Model model) {

        log.info("Generating calendar - {}", generateCalendarRequest);

        String chatId = UUID.randomUUID().toString();

        model.addAttribute("chatId", chatId);

        List<ArticleCalendarEntry> calendarEntries = marketingAssistant.getCalendar(chatId, generateCalendarRequest);

        log.info("Generated calendar - {}", calendarEntries);

        model.addAttribute("calendarEntries", calendarEntries);

        return "calendar :: #calendar";
    }

    @GetMapping("/questions")
    public String viewQuestions(Model model) {
        return "questions";
    }

    @PostMapping("/questions")
    public String generateQuestions(
            GenerateQuestionRequest generateQuestionRequest,
            Model model) {

        log.info("Generating questions - {}", generateQuestionRequest);

        String chatId = UUID.randomUUID().toString();

        model.addAttribute("chatId", chatId);

        List<String> questions = marketingAssistant.getQuestions(chatId, generateQuestionRequest);

        log.info("Generated questions - {}", questions);

        model.addAttribute("questions", questions);

        return "questionlist :: #questionlist";
    }
}
