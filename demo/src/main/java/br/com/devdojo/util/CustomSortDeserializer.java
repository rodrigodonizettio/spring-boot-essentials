package br.com.devdojo.util;

import java.io.IOException;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class CustomSortDeserializer extends JsonDeserializer<Sort> {

	@Override
	public Sort deserialize(JsonParser jsonParser,
			DeserializationContext deserializationContext) throws IOException {
		ArrayNode node = jsonParser.getCodec().readTree(jsonParser);
		Sort.Order[] orders = new Sort.Order[node.size()];
		int i = 0;
		for(JsonNode json : node) {
			orders[i] = new Sort.Order(Sort.Direction.valueOf(json.get("direction").asText()),
					json.get("property").asText());
			i++;
		}
		return new Sort(orders);
	}

}
