package com.bulgaria.musalasoft.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bulgaria.musalasoft.model.PeripheralD;
import com.bulgaria.musalasoft.services.PeripheralDService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Peripheral Device Controller")
public class PeripheralDController {

	@Autowired
	PeripheralDService peripheralDeviceService;

	@ApiOperation(value = "Find all peripheral devices", notes = "Returns all peripheral devices.")
	@GetMapping("/peripherals")
	public List<PeripheralD> getAllPeripheralDevices() {
		return peripheralDeviceService.getAllPeripheralDevices();
	}

	@ApiOperation(value = "Find peripheral devices, depending on if they are associated to a gateway or not", notes = "Returns the peripheral devices.")
	@GetMapping("/peripherals/associates/{a}")
	public List<PeripheralD> getPeripheralDevices(
			@ApiParam(value = "The value to determine if the peripheral devices will be those associated to a gateway or not.", required = true, example = "true") @PathVariable(value = "a") Boolean areAssociated) {
		return peripheralDeviceService.getPeripheralDevices(areAssociated);
	}

	@ApiResponses(value = { @ApiResponse(code = 404, message = "Not Found") })
	@ApiOperation(value = "Find a peripheral device by UID", notes = "Returns a peripheral device with the specified UID.")
	@GetMapping("/peripherals/{uid}")
	public PeripheralD getPeripheralDeviceByUID(
			@ApiParam(value = "The UID of the peripheral device.", required = true, example = "20013") @PathVariable(value = "uid") Integer peripheralDeviceUID) {
		PeripheralD result = peripheralDeviceService.getPeripheralDeviceByUID(peripheralDeviceUID); 
		return result;
	}

	@ApiResponses(value = { @ApiResponse(code = 409, message = "Conflict") })
	@ApiOperation(value = "Create a peripheral device", notes = "Returns the created peripheral device.")
	@PostMapping("/peripherals")
	public PeripheralD createPeripheralDevice(
			@ApiParam(value = "The information for a new peripheral device to be created.", required = true) @RequestBody @Valid PeripheralD peripheralDevice) {
		return peripheralDeviceService.createPeripheralDevice(peripheralDevice);
	}

	@ApiResponses(value = { @ApiResponse(code = 404, message = "Not Found") })
	@ApiOperation(value = "Update a peripheral device by UID ", notes = "Returns the peripheral device updated .")
	@PutMapping("/peripherals/{uid}")
	public PeripheralD updatePeripheralDevice(
			@ApiParam(value = "The UID of the peripheral device to be updated.", required = true, example = "20001") @PathVariable(value = "uid") Integer peripheralDeviceUID,
			@ApiParam(value = "The information for the peripheral device to be updated.", required = true, example = "holaMundo") @RequestBody @Valid PeripheralD peripheralDeviceDetails) {
		return peripheralDeviceService.updatePeripheralDevice(peripheralDeviceUID, peripheralDeviceDetails);
	}

	@ApiOperation(value = "Delete a peripheral device by UID", notes = "Deletes a peripheral device by UID.")
	@DeleteMapping("/peripherals/{uid}")
	public ResponseEntity<?> deletePeripheralDevice(
			@ApiParam(value = "The UID of the peripheral device to be deleted.", required = true, example = "25437") @PathVariable(value = "uid") Integer peripheralDeviceUID) {
		return peripheralDeviceService.deletePeripheralDevice(peripheralDeviceUID);
	}

}
