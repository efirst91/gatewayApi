package com.bulgaria.musalasoft.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.bulgaria.musalasoft.exceptions.AlreadyExistsIDException;
import com.bulgaria.musalasoft.exceptions.AlreadyExistsIPv4DException;
import com.bulgaria.musalasoft.exceptions.InvalidIPv4Exception;
import com.bulgaria.musalasoft.exceptions.ResourceNotFoundException;
import com.bulgaria.musalasoft.model.Gateway;

public class GatewayControllerTest extends ControllerTest {

	@Override
	@Before
	public void setUp() {
		super.setUp();
	}

	/**
	 * Testing the getAllGateways method of GatewayController
	 * 
	 * @throws Exception
	 */
	@Test
	public void testgetAllGateways() throws Exception {
		String uri = "http://localhost:8080/api/v1/gateways/";

		MvcResult mvcResult = mockMVc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

	}

	/**
	 * Testing the getGatewaysBySN method of GatewayController, with an existing
	 * serial number in the data base
	 * 
	 * @throws Exception
	 */
	@Test()
	public void testgetGatewayBySN1() throws Exception {
		String uri = "http://localhost:8080/api/v1/gateways/RECM12345M3C";

		MvcResult mvcResult = mockMVc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

	}

	/**
	 * Testing the getGatewaysBySN method of GatewayController, with a serial number
	 * that is not in data base
	 * 
	 * @throws Exception
	 * @throws ResourceNotFoundException
	 */
	@Test
	public void testgetGatewayBySN2() throws Exception {
		String uri = "http://localhost:8080/api/v1/gateways/RECM12345M8C";

		try {
			mockMVc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		} catch (ResourceNotFoundException e) {
			fail("Resource not found exception");
		}
	}

	/**
	 * Testing the createGateway method of GatewayController
	 * 
	 * @throws Exception
	 */
	@Test
	public void testcreateGateway1() throws Exception {
		String uri = "http://localhost:8080/api/v1/gateways/";

		Gateway gateway = new Gateway();
		gateway.setSerialNumber("FDW1558F2KP");
		gateway.setHumanReadableName("Admon RRHH");
		gateway.setIpv4Address("192.168.100.254");
		gateway.setPeripheralDevices(null);

		String inputJson = super.mapToJson(gateway);
		MvcResult mvcResult = mockMVc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}

	/**
	 * Testing the createGateway method of GatewayController, giving an existing
	 * serial number in the data base
	 * 
	 * @throws Exception
	 * @throws AlreadyExistsIDException
	 */
	@Test
	public void testcreateGateway2() throws Exception {
		String uri = "http://localhost:8080/api/v1/gateways/";

		Gateway gateway = new Gateway();
		gateway.setSerialNumber("RECM1234M1C");
		gateway.setIpv4Address("192.168.10.254");
		gateway.setHumanReadableName("Admon RRHH");
		gateway.setPeripheralDevices(null);

		String inputJson = super.mapToJson(gateway);
		try {
			mockMVc.perform(
					MockMvcRequestBuilders.post(uri).accept(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
					.andReturn();
		} catch (AlreadyExistsIDException e) {
			fail("Already exists gateway exception");
		}

	}

	/**
	 * Testing the createGateway method of GatewayController, giving an invalid
	 * ipv4Address
	 * 
	 * @throws Exception
	 * @throws InvalidIPv4Exception
	 */
	@Test
	public void testcreateGateway3() throws Exception {
		String uri = "http://localhost:8080/api/v1/gateways/";

		Gateway gateway = new Gateway();
		gateway.setSerialNumber("RECM1234M20C");
		gateway.setIpv4Address("NotValidIpv4");
		gateway.setHumanReadableName("RRHH");
		gateway.setPeripheralDevices(null);

		String inputJson = super.mapToJson(gateway);
		try {
			mockMVc.perform(
					MockMvcRequestBuilders.post(uri).accept(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
					.andReturn();
		} catch (InvalidIPv4Exception e) {
			fail("Ïnvalid ipv4 Address");
		}

	}

	/**
	 * Testing deleteGateway method of GatewayController, giving a serial number in
	 * the data base
	 * 
	 * @throws Exception
	 */
	@Test
	public void testdeleteGateway() throws Exception {
		String uri = "http://localhost:8080/api/v1/gateways/RECM12345M2C";

		MvcResult mvcResult = mockMVc
				.perform(MockMvcRequestBuilders.delete(uri).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}

	/**
	 * Testing updateGateway method of GatewayController, giving a serial number in
	 * the data base
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void testupdateGateway1() throws Exception {
		String uri = "http://localhost:8080/api/v1/gateways/RECM12345M6C";
		Gateway gateway = new Gateway();

		gateway.setHumanReadableName("ILEGAES");
		gateway.setIpv4Address("192.168.56.254");
		String inputJson = super.mapToJson(gateway);

		MvcResult mvcResult = mockMVc.perform(
				MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		gateway.setSerialNumber("RECM12345M6C");
		String outputJson = super.mapToJson(gateway);

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals(content, outputJson);

	}

	/**
	 * Testing updateGateway method of GatewayController, giving a Ipv4Address
	 * stored in the data base
	 * 
	 * @throws Exception
	 * @throws AlreadyExistsIPv4DException
	 * 
	 */
	@Test
	public void testupdateGateway2() throws Exception {
		String uri = "http://localhost:8080/api/v1/gateways/RECM12345M6C";
		Gateway gateway = new Gateway();

		gateway.setHumanReadableName("ILEGAES");
		gateway.setIpv4Address("192.168.200.254");
		String inputJson = super.mapToJson(gateway);

		try {
			mockMVc.perform(
					MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
					.andReturn();
		} catch (AlreadyExistsIPv4DException e) {
			fail("The provided ipv4Address already exist ");
		}

	}
}
