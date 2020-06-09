package cn.com.tiza.service;

import cn.com.tiza.api.GrampusApiService;
import cn.com.tiza.domain.DicItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * the implementation of interface GrampusApiService
 *
 * @author villas
 */
@Service
public class GrampusApiServiceImp implements GrampusApiService {

    @Autowired
    private TerminalService service;

    private static final String API_KEY = "API_KEY";

    @Override
    public String getApiKey(String terminalType) {
        List<DicItem> items = service.getOptionsByType(API_KEY);
        return items.stream()
                .filter(item -> Objects.equals(terminalType, item.getItemCode()))
                .map(DicItem::getItemValue)
                .findFirst()
                .orElse(null);
    }

}
