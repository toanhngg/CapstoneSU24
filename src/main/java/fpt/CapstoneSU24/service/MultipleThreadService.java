package fpt.CapstoneSU24.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
@Service
public class MultipleThreadService {
    private final UserService userService;
    private final ProductService productService;
    private final ItemService itemService;
    private final LocationService locationService;
    private final TransportService transportService;

    @Autowired
    public MultipleThreadService(TransportService transportService ,UserService userService, ProductService productService, ItemService itemService, LocationService locationService){
        this.userService = userService;
        this.productService = productService;
        this.itemService = itemService;
        this.locationService = locationService;
        this.transportService = transportService;
    }
    public JSONObject getQueryMultipleThreadForDatabase() throws IOException {
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDateTime firstDayOfMonthStart = firstDayOfMonth.atStartOfDay();
        long firstDayOfMonthTimestamp = firstDayOfMonthStart.toInstant(ZoneOffset.UTC).toEpochMilli();
        JSONObject status = new JSONObject();
        // Lấy timestamp hiện tại
        long currentTimestamp = Instant.now().toEpochMilli();
        int numThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);


        Callable<JSONObject> InfoUserTask = () -> userService.infoUserForMonitor(firstDayOfMonthTimestamp,currentTimestamp);
        Callable<JSONObject> InfoProductTask = () -> productService.infoProductForMonitor(firstDayOfMonthTimestamp,currentTimestamp);
        Callable<JSONObject> InfoItemTask = () -> itemService.infoItemForMonitor(firstDayOfMonthTimestamp,currentTimestamp);
        Callable<JSONObject> InfoLocationTask = locationService::PieCityForMonitor;
        Callable<JSONObject> NumberTransportTask = transportService::getForMonitor;
        Callable<JSONObject> ratioTask = itemService::logMetrics;

        Map<String, Callable<JSONObject>> tasks = new HashMap<>();
        tasks.put("InfoUserTask", InfoUserTask);
        tasks.put("InfoProductTask", InfoProductTask);
        tasks.put("InfoItemTask", InfoItemTask);
        tasks.put("InfoLocationTask", InfoLocationTask);
        tasks.put("NumberTransportTask", NumberTransportTask);
        tasks.put("ratioTask", ratioTask);




        Map<String, Future<JSONObject>> futures = new HashMap<>();

        try {
            for (Map.Entry<String, Callable<JSONObject>> entry : tasks.entrySet()) {
                Future<JSONObject> future = executorService.submit(entry.getValue());
                futures.put(entry.getKey(), future);
            }

            for (Map.Entry<String, Future<JSONObject>> entry : futures.entrySet()) {
                String key = entry.getKey();
                Future<JSONObject> future = entry.getValue();

                status.put(key, future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
        return status;
    }

}
