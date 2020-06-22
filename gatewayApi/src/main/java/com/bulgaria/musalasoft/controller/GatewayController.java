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

import com.bulgaria.musalasoft.model.Gateway;
import com.bulgaria.musalasoft.model.PeripheralD;
import com.bulgaria.musalasoft.services.GatewayService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Gateway Controller")
public class GatewayController {

	@Autowired
	GatewayService gatewayService;

	@ApiOperation(value = "Find all gateways", notes = "Returns all gateways.")
	@GetMapping("/gateways")
	public List<Gateway> getAllGateways() {
		return gatewayService.getAllGateways();
	}

	@ApiOperation(value = "Find a gateway by serial number", notes = "Returns a gateway with the specified serial number.")
	@GetMapping("/gateways/{sn}")
	public Gateway getGatewayBySN(
			@ApiParam(value = "The serial number of the gateway.", required = true, example = "RECM12345M8C") @PathVariable(value = "sn") String gatewaySN) {
		return gatewayService.getGatewayBySNu(gatewaySN);
	}

	@ApiOperation(value = "Find all peripheral devices of the specified gateway", notes = "Returns all peripheral devices of the specified gateway.")
	@GetMapping("/gateways/{sn}/peripherals")
	public List<PeripheralD> getAllPeripheralsByGatewayId(
			@ApiParam(value = "The serial number of the gateway.", required = true, example = "RECM12345M8C") @PathVariable(value = "sn") String gatewaySN) {
		return gatewayService.getAllPeripheralsByGatewayId(gatewaySN);
	}

	@ApiResponses(value = { @ApiResponse(code = 406, message = "Not Acceptable"),
			@ApiResponse(code = 409, message = "Conflict") })
	@ApiOperation(value = "Create a gateway", notes = "Returns the created gateway.")
	@PostMapping("/gateways")
	public Gateway createGateway(
			@ApiParam(value = "Gateway information for a new gateway to be created.", required = true) @RequestBody @Valid Gateway gateway) {
		return gatewayService.createGateway(gateway);
	}

	@ApiResponses(value = { @ApiResponse(code = 406, message = "Not Acceptable"),
			@ApiResponse(code = 409, message = "Conflict") })
	@ApiOperation(value = "Create a peripheral device for a determinate gateway", notes = "Returns the created peripheral device.")
	@PostMapping("/gateways/{sn}/peripherals")
	public PeripheralD createPeripheralDevice(
			@ApiParam(value = "The serial number of the gateway", required = true, example = "RECM12345M8C") @PathVariable(value = "sn") String gatewaySN,
			@ApiParam(value = "Peripheral device information for a new peripheral device to be created.", required = true) @RequestBody @Valid PeripheralD peripheralDevice) {
		return gatewayService.createPeripheralDeviceAndAssociatedGateway(gatewaySN, peripheralDevice);

	}

	@ApiResponses(value = { @ApiResponse(code = 406, message = "Not Acceptable") })
	@ApiOperation(value = "Associate  an existing peripheral device to a gateway", notes = "Associates  an existing peripheral device to a gateway.")
	@PutMapping("/gateways/{sn}/peripherals/{uid}")
	public ResponseEntity<?> assignAnExistingPeripheralDevice(
			@ApiParam(value = "The serial number of the gateway.", required = true, example = "RECM12345M1C") @PathVariable(value = "sn") String gatewaySN,
			@ApiParam(value = "The UID of the peripheral device to be associated.", required = true, example = "25437") @PathVariable(value = "uid") Integer peripheralDeviceUID) {
		ResponseEntity<?> result = gatewayService.associateAnExistingPeripheralDevice(gatewaySN, peripheralDeviceUID);
		return result;

	}

	@ApiResponses(value = { @ApiResponse(code = 406, message = "Not Acceptable") })
	@ApiOperation(value = "Update a gateway by serial number", notes = "Returns the updated gateway.")
	@PutMapping("/gateways/{sn}")
	public Gateway updateGateway(
			@ApiParam(value = "The serial number of the gateway to be updated.", required = true, example = "RECM12345M3C") @PathVariable(value = "sn") String gatewaySN,
			@ApiParam(value = "Gateway information for the gateway to be updated.", required = true) @Valid @RequestBody Gateway gatewayDetails) {
		Gateway result = gatewayService.updateGateway(gatewaySN, gatewayDetails);
		return result;

	}

	@ApiResponses(value = { @ApiResponse(code = 424, message = "Failed Dependency") })
	@ApiOperation(value = "Delete a peripheral device from a gateway", notes = "Deletes a peripheral device from a gateway.")
	@DeleteMapping("/gateways/{sn}/peripherals/{uid}")
	public ResponseEntity<?> deletePeripheralDevice(
			@ApiParam(value = "The serial number of the gateway.", required = true, example = "RECM12345M3C") @PathVariable(value = "sn") String gatewaySN,
			@ApiParam(value = "The UID of the peripheral device to be deleted.", required = true, example = "25437") @PathVariable(value = "uid") Integer peripheralDeviceUID) {
		return gatewayService.deletePeripheralDeviceAssociated(gatewaySN, peripheralDeviceUID);

	}

	@ApiOperation(value = "Delete a gateway by serial number", notes = "Deletes a gateway by serial number.")
	@DeleteMapping("/gateways/{sn}")
	public ResponseEntity<?> deleteGateway(
			@ApiParam(value = "The serial number of the gateway to be deleted.", required = true, example = "RECM12345M4C") @PathVariable(value = "sn") String gatewaySN) {
		return gatewayService.deleteGateway(gatewaySN);
	}

}
