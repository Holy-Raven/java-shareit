package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class itemRequestServiceImpl implements ItemRequestService {

    @Transactional
    @Override
    public ItemRequestDto addRequest(ItemRequestDto itemRequestDto, long userId) {
        return null;
    }

    @Override
    public List<ItemRequestDto> getRequests(long userId) {
        return null;
    }

    @Override
    public List<ItemRequestDto> getAllRequests(long from, long size) {
        return null;
    }

    @Override
    public ItemRequestDto getRequestById(long userId, long requestId) {
        return null;
    }
}
