package ai.movyra.application;

import ai.movyra.application.port.in.appointment.CreateAppointmentUseCase;
import ai.movyra.application.port.out.AppointmentRepository;
import ai.movyra.application.service.appointment.AppointmentService;
import ai.movyra.domain.exception.AppointmentConflictException;
import ai.movyra.domain.model.Appointment;
import ai.movyra.domain.model.valueobject.AppointmentStatus;
import ai.movyra.domain.model.valueobject.MoneyCents;
import ai.movyra.domain.model.valueobject.TenantId;
import ai.movyra.domain.model.valueobject.TimeRange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAppointmentUseCaseTest {
    
    @Mock
    private AppointmentRepository appointmentRepository;
    
    private AppointmentService appointmentService;
    
    private final TenantId tenantId = TenantId.generate();
    private final UUID professionalId = UUID.randomUUID();
    private final UUID customerId = UUID.randomUUID();
    private final UUID serviceId = UUID.randomUUID();
    
    @BeforeEach
    void setUp() {
        appointmentService = new AppointmentService(appointmentRepository);
    }
    
    @Test
    void shouldCreateAppointmentWhenNoConflicts() {
        // Given
        Instant start = Instant.parse("2024-01-15T10:00:00Z");
        Instant end = Instant.parse("2024-01-15T11:00:00Z");
        
        when(appointmentRepository.findByProfessionalAndTimeRange(any(), any(), any(), any()))
            .thenReturn(List.of());
        
        when(appointmentRepository.save(any()))
            .thenAnswer(invocation -> invocation.getArgument(0));
        
        CreateAppointmentUseCase.CreateAppointmentCommand command = 
            new CreateAppointmentUseCase.CreateAppointmentCommand(
                tenantId,
                customerId,
                    professionalId,
                serviceId,
                start,
                end,
                5000,
                "Test appointment"
            );
        
        // When
        Appointment result = appointmentService.create(command);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTenantId()).isEqualTo(tenantId);
        assertThat(result.getProfessionalId()).isEqualTo(professionalId);
        assertThat(result.getStartAt()).isEqualTo(start);
        assertThat(result.getEndAt()).isEqualTo(end);
        assertThat(result.getStatus()).isEqualTo(AppointmentStatus.REQUESTED);
        
        verify(appointmentRepository).save(any());
    }
    
    @Test
    void shouldRejectAppointmentWhenConflictExists() {
        // Given
        Instant start = Instant.parse("2024-01-15T10:30:00Z");
        Instant end = Instant.parse("2024-01-15T11:30:00Z");
        
        Appointment existingAppointment = createExistingAppointment(
            "2024-01-15T10:00:00Z",
            "2024-01-15T11:00:00Z"
        );
        
        when(appointmentRepository.findByProfessionalAndTimeRange(any(), any(), any(), any()))
            .thenReturn(List.of(existingAppointment));
        
        CreateAppointmentUseCase.CreateAppointmentCommand command = 
            new CreateAppointmentUseCase.CreateAppointmentCommand(
                tenantId,
                customerId,
                    professionalId,
                serviceId,
                start,
                end,
                5000,
                "Test appointment"
            );
        
        // When & Then
        assertThatThrownBy(() -> appointmentService.create(command))
            .isInstanceOf(AppointmentConflictException.class)
            .hasMessageContaining(professionalId.toString());
        
        verify(appointmentRepository, never()).save(any());
    }
    
    @Test
    void shouldAllowAppointmentWhenConflictIsCancelled() {
        // Given
        Instant start = Instant.parse("2024-01-15T10:30:00Z");
        Instant end = Instant.parse("2024-01-15T11:30:00Z");
        
        Appointment cancelledAppointment = createCancelledAppointment(
            "2024-01-15T10:00:00Z",
            "2024-01-15T11:00:00Z"
        );
        
        when(appointmentRepository.findByProfessionalAndTimeRange(any(), any(), any(), any()))
            .thenReturn(List.of(cancelledAppointment));
        
        when(appointmentRepository.save(any()))
            .thenAnswer(invocation -> invocation.getArgument(0));
        
        CreateAppointmentUseCase.CreateAppointmentCommand command = 
            new CreateAppointmentUseCase.CreateAppointmentCommand(
                tenantId,
                customerId,
                professionalId,
                serviceId,
                start,
                end,
                5000,
                null
            );
        
        // When
        Appointment result = appointmentService.create(command);
        
        // Then
        assertThat(result).isNotNull();
        verify(appointmentRepository).save(any());
    }
    
    @Test
    void shouldCheckRepositoryWithCorrectParameters() {
        // Given
        Instant start = Instant.parse("2024-01-15T10:00:00Z");
        Instant end = Instant.parse("2024-01-15T11:00:00Z");
        
        when(appointmentRepository.findByProfessionalAndTimeRange(any(), any(), any(), any()))
            .thenReturn(List.of());
        
        when(appointmentRepository.save(any()))
            .thenAnswer(invocation -> invocation.getArgument(0));
        
        CreateAppointmentUseCase.CreateAppointmentCommand command = 
            new CreateAppointmentUseCase.CreateAppointmentCommand(
                tenantId,
                customerId,
                professionalId,
                serviceId,
                start,
                end,
                5000,
                null
            );
        
        // When
        appointmentService.create(command);
        
        // Then
        verify(appointmentRepository).findByProfessionalAndTimeRange(
            eq(tenantId),
            eq(professionalId),
            eq(start),
            eq(end)
        );
    }
    
    private Appointment createExistingAppointment(String startStr, String endStr) {
        TimeRange timeRange = TimeRange.of(
            Instant.parse(startStr),
            Instant.parse(endStr)
        );
        
        return new Appointment(
            UUID.randomUUID(),
            tenantId,
            customerId,
            professionalId,
            serviceId,
            timeRange,
            AppointmentStatus.CONFIRMED,
            MoneyCents.of(5000)
        );
    }
    
    private Appointment createCancelledAppointment(String startStr, String endStr) {
        TimeRange timeRange = TimeRange.of(
            Instant.parse(startStr),
            Instant.parse(endStr)
        );
        
        return new Appointment(
            UUID.randomUUID(),
            tenantId,
            customerId,
            professionalId,
            serviceId,
            timeRange,
            AppointmentStatus.CANCELLED,
            MoneyCents.of(5000)
        );
    }
}
