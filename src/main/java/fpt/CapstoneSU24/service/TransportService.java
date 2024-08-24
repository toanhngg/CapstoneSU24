package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.model.Transport;
import fpt.CapstoneSU24.repository.TransportRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransportService {

    private final TransportRepository transportRepository;
    private final ELKService elkService;
    @Autowired
    public TransportService(TransportRepository transportRepository, ELKService elkService) {
        this.transportRepository = transportRepository;
        this.elkService = elkService;
    }

    public ResponseEntity<?> getAllTransport() {
        List<Transport> transport = transportRepository.findAll();
        return ResponseEntity.ok(transport);
    }
    public JSONObject getForMonitor() {
        JSONArray jsonArray = new JSONArray();
        List<Transport> transports = transportRepository.findAll();
        for (Transport t : transports) {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("key", t.getTransportName());
            jsonObject.put("shortKey", t.getShortName());
            jsonObject.put("value", elkService.getNumberTransport(t.getTransportId()));

            jsonArray.put(jsonObject);
        }
        return new JSONObject().put("transport", jsonArray);
    }
}
