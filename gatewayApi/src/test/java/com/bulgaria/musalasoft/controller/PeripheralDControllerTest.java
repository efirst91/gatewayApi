package com.bulgaria.musalasoft.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.bulgaria.musalasoft.exceptions.AlreadyExistsIDException;
import com.bulgaria.musalasoft.model.PeripheralD;

public class PeripheralDControllerTest extends ControllerTest {

	@Override
	@Before
	public void setUp() {
		super.setUp();
	}

	/**
	 * Testing the getAllPeripheralD method of PeripheralDController
	 * 
	 * @throws Exception
	 */
	@Test
	public void testgetAllPeripheralD() throws Exception {
		String uri = "http://localhost:8080/api/v1/peripherals/";

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
	public void testgetPeripheralDeviceByUID() throws Exception {
		String uri = "http://localhost:8080/api/v1/peripherals/20007";

		MvcResult mvcResult = mockMVc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

	}

	/**
	 * Testing the createPeripheralDevice method of PeripheralDController
	 * 
	 * @throws Exception
	 */
	@Test
	public void testcreatePeripheralDevice1() throws Exception {
		String uri = "http://localhost:8080/api/v1/peripherals/";

		Date dateCreated = new Date();
		PeripheralD peripheralD = new PeripheralD();
		peripheralD.setUid(20054);
		peripheralD.setVendor("OCEAN");
		peripheralD.setDateCreated(dateCreated);
		peripheralD.setStatusPd(false);

		String inputJson = super.mapToJson(peripheralD);
		MvcResult mvcResult = mockMVc
				.perform(MockMvcRequestBuilders.post(uri).accept(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

	}

	/**
	 * Testing the createPeripheralDevice method of Peripheral Controller, giving an
	 * existence peripheral device with the same UID
	 * 
	 * @throws Exception
	 * @throws AlreadyExistsIDException
	 */
	@Test
	public void testcreatePeripheralDevice2() throws Exception {
		String uri = "http://localhost:8080/api/v1/peripherals/";

		Date dateCreated = new Date();
		PeripheralD peripheralD = new PeripheralD();
		peripheralD.setUid(20028);
		peripheralD.setVendor("OCEAN");
		peripheralD.setDateCreated(dateCreated);
		peripheralD.setStatusPd(false);

		String inputJson = super.mapToJson(peripheralD);
		try {
			mockMVc.perform(
					MockMvcRequestBuilders.post(uri).accept(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
					.andReturn();

		} catch (AlreadyExistsIDException e) {
			fail("Already exist exception");
			;
		}

	}

	/**
	 * Testing the deletePeripheralDevice method of PeripheralDController
	 * 
	 * @throws Exception
	 */
	@Test
	public void testdeletePeripheralDevice() throws Exception {
		String uri = "http://localhost:8080/api/v1/peripherals/20013";

		MvcResult mvcResult = mockMVc
				.perform(MockMvcRequestBuilders.delete(uri).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

	}

	/**
	 * Testing updatePeripheralDevice method of PeripheralDController, giving a UID
	 * in the data base
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void testupdatePeripheralDevice1() throws Exception {
		String uri = "http://localhost:8080/api/v1/peripherals/20007";
		PeripheralD pdToUpdate = new PeripheralD();

		pdToUpdate.setVendor("Canon Updated");
		pdToUpdate.setDateCreated(new Date());
		pdToUpdate.setStatusPd(false);

		String inputJson = super.mapToJson(pdToUpdate);

		MvcResult mvcResult = mockMVc.perform(
				MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		assertTrue(!content.isEmpty());
	}

	/**
	 * Testing updatePeripheralDevice method of PeripheralDController, giving a UID that
	 * has not stored in the data base
	 * 
	 * @throws Exception
	 * @throws ResourceNotFoundException
	 * 
	 */
	@Test
	public void testupdatePeripheralDevice2() throws Exception {
		String uri = "http://localhost:8080/api/v1/peripherals/2000090";
		PeripheralD pdToUpdate = new PeripheralD();

		pdToUpdate.setVendor("Canon Updated");
		pdToUpdate.setDateCreated(new Date());
		pdToUpdate.setStatusPd(false);

		String inputJson = super.mapToJson(pdToUpdate);

		try {
			mockMVc.perform(
					MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
					.andReturn();
		} catch (Exception e) {
			fail("Resource not found exception");
		}

	}
}
