package com.tae.Etickette.concert.command.application;

import com.tae.Etickette.concert.command.application.dto.RegisterConcertRequest;
import com.tae.Etickette.concert.command.domain.Concert;
import com.tae.Etickette.concert.command.domain.ConcertCreatedEvent;
import com.tae.Etickette.concert.command.domain.ImageUploader;
import com.tae.Etickette.concert.infra.ConcertRepository;
import com.tae.Etickette.global.event.Events;
import com.tae.Etickette.global.exception.ServerProcessException;
import com.tae.Etickette.testhelper.ConcertCreateBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit - RegisterConcertService")
class RegisterConcertServiceTest {
    @InjectMocks
    private RegisterConcertService registerConcertService;
    private ImageUploader imageUploader = mock(ImageUploader.class);
    private ConcertRepository concertRepository = mock(ConcertRepository.class);
    private final ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);
    @BeforeEach
    void setUp() {
        Events.setPublisher(publisher);
    }

    @Test
    @DisplayName("이미지 업로드에 실패하면, 공연등록에 실패한다.")
    void 공연등록_실패_이미지업로드실패() {
        //given
        RegisterConcertRequest request = ConcertCreateBuilder.builder()
                .title("공연A")
                .overview("공연A입니다.")
                .runningTime(120).build();
        MockMultipartFile multipartFile = new MockMultipartFile(
                "image",
                "testImage.png",
                "image/png",
                "이미지데이터".getBytes()
        );
        BDDMockito.given(imageUploader.upload(any())).willThrow(ServerProcessException.class);

        //when & then
        Assertions.assertThrows(ServerProcessException.class,
                () -> registerConcertService.register(request, multipartFile));
    }

    @Test
    @DisplayName("이미지 업로드에 성공하면, 공연등록에 성공한다.")
    void 공연등록_성공_이미지업로드성공() {
        //given
        RegisterConcertRequest request = ConcertCreateBuilder.builder()
                .title("공연A")
                .overview("공연A입니다.")
                .runningTime(120).build();
        MockMultipartFile multipartFile = new MockMultipartFile(
                "image",
                "testImage.png",
                "image/png",
                "이미지데이터".getBytes()
        );
        BDDMockito.given(imageUploader.upload(any())).willReturn("/imgPath");
        BDDMockito.given(concertRepository.save(any())).willReturn(mock(Concert.class));

        //when & then
        Assertions.assertDoesNotThrow(() -> registerConcertService.register(request, multipartFile));
    }

    @Test
    @DisplayName("공연을 등록하면, ConcertCreatedEvent 가 발행되어야 한다.")
    void 공연등록_좌석생성이벤트_발행() {
        //given
        MultipartFile file = mock(MultipartFile.class);
        RegisterConcertRequest request = ConcertCreateBuilder.builder().build();

        Concert savedConcert = mock(Concert.class);
        BDDMockito.given(savedConcert.getId()).willReturn(1L);
        BDDMockito.given(concertRepository.save(any(Concert.class))).willReturn(savedConcert);

        //when
        registerConcertService.register(request, file);

        //then
        verify(publisher, times(1))
                .publishEvent(any(ConcertCreatedEvent.class));
    }
}