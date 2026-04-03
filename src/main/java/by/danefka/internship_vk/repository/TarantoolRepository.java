package by.danefka.internship_vk.repository;

import by.danefka.internship_vk.model.Kv;
import io.tarantool.client.box.TarantoolBoxClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TarantoolRepository {
    private final TarantoolBoxClient client;


    public void put(String key, byte[] value) {
        try {
            client.call("kv_put", Arrays.asList(key, value)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] get(String key) {
        try {
            var response = client.call("kv_get", List.of(key)).get();
            var data = response.get();

            if (data == null || data.isEmpty() || data.get(0) == null) {
                throw new RuntimeException("Key not found");
            }

            List<?> tuple = (List<?>) data.get(0);
            Object rawValue = tuple.get(1);

            return rawValue == null ? null : (byte[]) rawValue;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String key) {
        try {
            client.call("kv_delete", List.of(key)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Kv> range(String keyFrom, String keyTo) {
        try {
            var response = client.call("kv_range", List.of(keyFrom, keyTo)).get();
            var data = response.get();

            if (data == null || data.isEmpty() || data.get(0) == null) {
                return List.of();
            }

            List<?> rows = (List<?>) data.get(0);

            return rows.stream()
                    .map(rowObj -> {
                        List<?> tuple = (List<?>) rowObj;
                        String key = (String) tuple.get(0);

                        Object rawValue = tuple.get(1);
                        byte[] value = rawValue == null ? null : (byte[]) rawValue;

                        return new Kv(key, value);
                    })
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int count() {
        try {
            var response = client.call("kv_count").get();
            Object value = response.get().get(0);

            if (value == null) {
                return 0;
            }

            return ((Number) value).intValue();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}