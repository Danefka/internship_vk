package by.danefka.internship_vk.service;

import by.danefka.internship_vk.model.Kv;
import by.danefka.internship_vk.repository.TarantoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KvService {

    private final TarantoolRepository repository;


    public void put(String key, String value) {
        byte[] bytes = value == null ? null : value.getBytes(StandardCharsets.UTF_8);
        repository.put(key, bytes);
    }

    public byte[] get(String key) {
        return repository.get(key);
    }

    public void delete(String key) {
        repository.delete(key);
    }

    public List<Kv> range(String keyFrom, String keyTo) {
        return repository.range(keyFrom, keyTo);
    }

    public Integer count() {
        return repository.count();
    }
}
