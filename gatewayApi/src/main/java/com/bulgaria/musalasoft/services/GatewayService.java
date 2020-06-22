package com.bulgaria.musalasoft.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bulgaria.musalasoft.exceptions.AlreadyExistsIDException;
import com.bulgaria.musalasoft.exceptions.AlreadyExistsIPv4DException;
import com.bulgaria.musalasoft.exceptions.InvalidIPv4Exception;
import com.bulgaria.musalasoft.exceptions.MaxLimitReachedException;
import com.bulgaria.musalasoft.exceptions.NotOwnerException;
import com.bulgaria.musalasoft.exceptions.ResourceNotFoundException;
import com.bulgaria.musalasoft.model.Gateway;
import com.bulgaria.musalasoft.model.PeripheralD;
import com.bulgaria.musalasoft.repository.GatewayRepository;
import com.bulgaria.musalasoft.repository.PeripheralDRepository;

@Service
public class GatewayService {

	@Autowired
	GatewayRepository gatewayRepository;
	@Autowired
	PeripheralDRepository peripheralDeviceRepository;

	/**
	 * Find all gateways
	 * 
	 * @return all existing gateways in the database
	 */
	public List<Gateway> getAllGateways() {
		return gatewayRepository.findAll();

	}

	/**
	 * Find the gateway with the specified serial number
	 * 
	 * @param gatewaySN serial number of the gateway
	 * @throws ResourceNotFoundException if there is no gateway with the specified
	 *                                   serial number
	 * @return the gateway with the specified serial number
	 * 
	 */
	public Gateway getGatewayBySNu(String gatewaySN) {
		return gatewayRepository.findById(gatewaySN)
				.orElseThrow(() -> new ResourceNotFoundException("Gateway", "serial number", gatewaySN));
	}

	/**
	 * Find the peripheral devices associated to the gateway
	 * 
	 * @param gatewaySN serial number of the gateway
	 * @throws ResourceNotFoundException if there is no gateway with the specified
	 *                                   serial number
	 * @return the list of peripheral devices
	 *
	 */
	public List<PeripheralD> getAllPeripheralsByGatewayId(String gatewaySN) {
		Gateway gateway = gatewayRepository.findById(gatewaySN)
				.orElseThrow(() -> new ResourceNotFoundException("Gateway", "serial number", gatewaySN));

		return gateway.getPeripheralDevices();
	}

	/**
	 * Store a gateway in the database
	 * 
	 * @param gateway information for a new gateway to be created
	 * @throws AlreadyExistsIDException    if a gateway with serial number already
	 *                                     exists or if any of peripheral devices
	 *                                     already exists with the provided UIDs
	 * @throws InvalidIPv4Exception        if the ipv4 address is not valid
	 * @throws AlreadyExistsIPv4DException if the ipv4 address is already being used
	 * @return the gateway created
	 *
	 */
	public Gateway createGateway(Gateway gateway) {
		if (gatewayRepository.findById(gateway.getSerialNumber()).isPresent()) {
			throw new AlreadyExistsIDException("Gateway", "serial number", gateway.getSerialNumber());
		}
		if (!isValidIp4Address(gateway.getIpv4Address())) {
			throw new InvalidIPv4Exception("Gateway", "Ipv4 Address", gateway.getIpv4Address());
		}
		if (gatewayRepository.findByIpv4Address(gateway.getIpv4Address()) != null) {
			throw new AlreadyExistsIPv4DException("Gateway", "Ipv4 Address", gateway.getIpv4Address());
		}

		List<PeripheralD> peripheralDevices = gateway.getPeripheralDevices();

		if (peripheralDevices != null && !peripheralDevices.isEmpty()) {
			for (PeripheralD pd : peripheralDevices) {
				if (peripheralDeviceRepository.findById(pd.getUid()).isPresent()) {
					throw new AlreadyExistsIDException("PeripheralDevice", "UID", pd.getUid());
				}
			}
		}

		gateway.setPeripheralDevices(null);
		Gateway createGateway = gatewayRepository.save(gateway);

		if (peripheralDevices != null && !peripheralDevices.isEmpty()) {
			for (PeripheralD pd : peripheralDevices) {
				pd.setGateway(gateway);
				peripheralDeviceRepository.save(pd);
			}
		}

		createGateway.setPeripheralDevices(peripheralDevices);
		return createGateway;
	}

	/**
	 * Store a peripheral device in the database and associate it to the gateway
	 * with the specified serial number
	 * 
	 * @param gatewaySN        serial number of the gateway
	 * @param peripheralDevice information for a new peripheral device to be created
	 * @throws ResourceNotFoundException if there is no gateway with the specified
	 *                                   serial number
	 * @throws AlreadyExistsIDException  if a peripheral device with UID already
	 *                                   exists
	 * @throws MaxLimitReachedException  if gateway with the specified serial
	 *                                   number, has 10 peripheral devices
	 *                                   associated
	 * @return the peripheral device created
	 *
	 */
	public PeripheralD createPeripheralDeviceAndAssociatedGateway(String gatewaySN, PeripheralD peripheralDevice) {
		Gateway gateway = gatewayRepository.findById(gatewaySN)
				.orElseThrow(() -> new ResourceNotFoundException("Gateway", "serial number", gatewaySN));

		if (peripheralDeviceRepository.findById(peripheralDevice.getUid()).isPresent()) {
			throw new AlreadyExistsIDException("PeripheralDevice", "UID", peripheralDevice.getUid());
		}

		if (gateway.getPeripheralDevices().size() < 10) {
			peripheralDevice.setGateway(gateway);
			return peripheralDeviceRepository.save(peripheralDevice);
		} else {
			throw new MaxLimitReachedException("Gateway", "serial number", gateway.getSerialNumber());
		}

	}

	/**
	 * Associate an existing peripheral device with the specified UID, to gateway
	 * with the specified serial number
	 * 
	 * @param gatewaySN           serial number of the gateway
	 * @param peripheralDeviceUID UID of the peripheral device
	 * @throws ResourceNotFoundException if there is no gateway with the specified
	 *                                   serial number or if there is no peripheral
	 *                                   device with the specified UID
	 * @throws MaxLimitReachedException  if gateway with the specified serial
	 *                                   number, has 10 peripheral devices
	 *                                   associated
	 * @return HttpStatus.OK if the peripheral device was successfully associated
	 * 
	 */
	public ResponseEntity<?> associateAnExistingPeripheralDevice(String gatewaySN, Integer peripheralDeviceUID) {

		Gateway gateway = gatewayRepository.findById(gatewaySN)
				.orElseThrow(() -> new ResourceNotFoundException("Gateway", "serial number", gatewaySN));

		PeripheralD peripheralDevice = peripheralDeviceRepository.findById(peripheralDeviceUID)
				.orElseThrow(() -> new ResourceNotFoundException("PeripheralDevice", "UID", peripheralDeviceUID));

		if (gateway.getPeripheralDevices().size() < 10) {
			peripheralDevice.setGateway(gateway);
			peripheralDeviceRepository.save(peripheralDevice);
			return ResponseEntity.ok().build();
		} else
			throw new MaxLimitReachedException("Gateway", "serial number", gateway.getSerialNumber());
	}

	/**
	 * Update a gateway with the specified serial number
	 * 
	 * @param gatewaySN      serial number of the gateway
	 * @param gatewayDetails gateway information for the gateway to be updated
	 * @throws ResourceNotFoundException   if there is no gateway with the specified
	 *                                     serial number
	 * @throws InvalidIPv4Exception        if the ipv4 address is not valid
	 * @throws AlreadyExistsIPv4DException if the ipv4 address is already being used
	 * @return the gateway updated
	 * 
	 */
	public Gateway updateGateway(String gatewaySN, Gateway gatewayDetails) {

		if (!isValidIp4Address(gatewayDetails.getIpv4Address())) {
			throw new InvalidIPv4Exception("Gateway", "Ipv4 Address", gatewayDetails.getIpv4Address());
		}
		
		Gateway checkIfExistSn = gatewayRepository.findById(gatewaySN)
				.orElseThrow(() -> new ResourceNotFoundException("Gateway", "serial number", gatewaySN));

		

		if (!gatewayDetails.getIpv4Address().isEmpty() && gatewayDetails.getIpv4Address().equalsIgnoreCase(checkIfExistSn.getIpv4Address())) {
			throw new AlreadyExistsIPv4DException("Gateway", "Ipv4 Address", gatewayDetails.getIpv4Address());
		}
		
		 
		gatewayDetails.setSerialNumber(gatewaySN);

		return gatewayRepository.save(gatewayDetails);
	}

	/**
	 * Delete peripheral device with the specified UID, if it is associated to
	 * gateway with the specified serial number
	 * 
	 * @param gatewaySN           serial number of the gateway
	 * @param peripheralDeviceUID UID of the peripheral device
	 * @throws ResourceNotFoundException if there is no gateway with the specified
	 *                                   serial number or if there is no peripheral
	 *                                   device with the specified UID
	 * @throws NotOwnerException         if peripheral device with the specified
	 *                                   UID, it is not associated with gateway with
	 *                                   the specified serial number
	 * @return HttpStatus.OK if the peripheral device was successfully deleted
	 * 
	 */
	public ResponseEntity<?> deletePeripheralDeviceAssociated(String gatewaySN, Integer peripheralDeviceUID) {
		Gateway gateway = gatewayRepository.findById(gatewaySN)
				.orElseThrow(() -> new ResourceNotFoundException("Gateway", "serial number", gatewaySN));

		PeripheralD peripheralDevice = peripheralDeviceRepository.findById(peripheralDeviceUID)
				.orElseThrow(() -> new ResourceNotFoundException("PeripheralDevice", "UID", peripheralDeviceUID));

		if (!gateway.equals(peripheralDevice.getGateway())) {
			throw new NotOwnerException("Gateway", "serial number", gatewaySN, "PeripheralDevice", "UID",
					peripheralDeviceUID);
		}

		peripheralDeviceRepository.delete(peripheralDevice);
		return ResponseEntity.ok().build();

	}

	/**
	 * Delete the gateway with the specified serial number
	 * 
	 * @param gatewaySN serial number of the gateway
	 * @throws ResourceNotFoundException if there is no gateway with the specified
	 *                                   serial number
	 * @return HttpStatus.OK if the gateway was successfully deleted
	 * 
	 */
	public ResponseEntity<?> deleteGateway(String gatewaySN) {
		Gateway gateway = gatewayRepository.findById(gatewaySN)
				.orElseThrow(() -> new ResourceNotFoundException("Gateway", "serial number", gatewaySN));

		gatewayRepository.delete(gateway);
		return ResponseEntity.ok().build();
	}

	/**
	 * Verify ipv4 address (Valid or Not)
	 * 
	 * @param ipv4Address
	 * @return true if the specified ipv4 address is valid
	 */
	private boolean isValidIp4Address(String ipv4Address) {
		InetAddressValidator validator = InetAddressValidator.getInstance();
		return validator.isValidInet4Address(ipv4Address);
	}

}
