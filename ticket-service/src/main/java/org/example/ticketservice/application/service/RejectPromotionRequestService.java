package org.example.ticketservice.application.service;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.example.ticketservice.application.command.RejectPromotionRequestCommand;
import org.example.ticketservice.application.usecase.RejectPromotionRequestUseCase;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RejectPromotionRequestService implements RejectPromotionRequestUseCase {

    private final TaskService taskService;

    @Override
    public void reject(RejectPromotionRequestCommand command) {
        Task task = taskService.createTaskQuery()
                .processVariableValueEquals("promotionTicketId", command.promotionTicketId().toString())
                .taskDefinitionKey("consider-the-request")
                .singleResult();

        if (task == null) {
            throw new IllegalStateException(
                    "No pending review task found for promotionTicketId: " + command.promotionTicketId());
        }

        taskService.complete(task.getId(), Map.of("approved", false));
    }
}
