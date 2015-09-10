package org.yeastrc.paws.utils;

import java.io.ByteArrayOutputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GetJSONForObject {

	/**
	 * @param objectToSerialize
	 * @return
	 * @throws Exception
	 */
	public static String getJSONForObject ( Object objectToSerialize ) throws Exception {
		

		// build the JSON data structure for result
		
		ObjectMapper mapper = new ObjectMapper();  //  Jackson JSON library object
		ByteArrayOutputStream responseJSONBAOS = new ByteArrayOutputStream( 100000 );
		mapper.writeValue( responseJSONBAOS, objectToSerialize ); // where first param can be File, OutputStream or Writer

		String jsonAsString = responseJSONBAOS.toString();
		
		return jsonAsString;
	}
}
