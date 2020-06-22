package com.bulgaria.musalasoft.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bulgaria.musalasoft.exceptions.AlreadyExistsIDException;
import com.bulgaria.musalasoft.exceptions.BlankFieldsException;
import com.bulgaria.musalasoft.exceptions.ResourceNotFoundException;
import com.bulgaria.musalasoft.model.PeripheralD;
import com.bulgaria.musalasoft.repository.PeripheralDRepository;

@Service
public class PeripheralDService {

	@Autowired
	PeripheralDRepository peripheralDRepository;

	/**
	 * Find all peripheral devices
	 * 
	 * @return all existing peripheral devices in the database
	 */
	public List<PeripheralD> getAllPeripheralDevices() {
		return peripheralDRepository.findAll();
	}

	/**
	 * Find peripheral devices associated to a gateway or not
	 * 
	 * @param areAssociated determine if the peripheral devices to return will be
	 *                      the associated ones or not
	 * @return the list of peripheral devices
	 */
	public List<PeripheralD> getPeripheralDevices(Boolean flagAssociated) {
		if (flagAssociated) {
			return peripheralDRepository.findByGatewayNotNull();
		}
		return peripheralDRepository.findByGatewayNull();
	}

	/**
	 * Find the peripheral device with the specified UID
	 * 
	 * @param peripheralDeviceUID UID of the peripheral device
	 * @throws ResourceNotFoundException if there is no peripheral device with the
	 *                                   specified UID
	 * @return the peripheral device with the specified UID
	 * 
	 */
	public PeripheralD getPeripheralDeviceByUID(Integer peripheralDeviceUID) {
		return peripheralDRepository.findById(peripheralDeviceUID).orElseThrow(
				() -> new ResourceNotFoundException("PeripheralDevice", "serial number", peripheralDeviceUID));
	}

	/**
	 * Store a peripheral device in the database
	 * 
	 * @param peripheralDevice information for a new peripheral device to be created
	 * @throws AlreadyExistsIDException if a peripheral device with UID already
	 *                                  exists
	 * @return the peripheral device created
	 * 
	 */
	public PeripheralD createPeripheralDevice(PeripheralD peripheralDevice) {

		if (peripheralDevice.getUid() == 0 || peripheralDevice.getVendor().isEmpty()) {
			throw new BlankFieldsException("Create", "Peripheral device");
		}
		if (peripheralDRepository.findById(peripheralDevice.getUid()).isPresent()) {
			throw new AlreadyExistsIDException("PeripheralDevice", "UID", peripheralDevice.getUid());
		}

		return peripheralDRepository.save(peripheralDevice);
	}

	/**
	 * Update a peripheral device with the specified UID
	 * 
	 * @param peripheralDeviceUID     UID of the peripheral device
	 * @param peripheralDeviceDetails information for the peripheral device to be
	 *                                updated
	 * @throws ResourceNotFoundException if there is no peripheral device with the
	 *                                   specified UID
	 * @return the peripheral device updated
	 */
	public PeripheralD updatePeripheralDevice(Integer peripheralDeviceUID, PeripheralD peripheralDeviceDetails) {

		PeripheralD existence = peripheralDRepository.findById(peripheralDeviceUID).orElseThrow(
				() -> new ResourceNotFoundException("PeripheralDevice", "serial number", peripheralDeviceUID));
		
		if (peripheralDeviceDetails.getDateCreated() == null) {
			peripheralDeviceDetails.setDateCreated(existence.getDateCreated());
		} else if (peripheralDeviceDetails.getVendor().equals("")) {
			peripheralDeviceDetails.setVendor(existence.getVendor());
		}
		
		try {
			peripheralDeviceDetails.isStatusPd();
		} catch (BlankFieldsException e) {
			new BlankFieldsException("Update Peripheral", "Peripheral Service");
		}
		
		peripheralDeviceDetails.setGateway(existence.getGateway());
		peripheralDeviceDetails.setUid(peripheralDeviceUID);
		PeripheralD updatedPeripheralDevice = peripheralDRepository.save(peripheralDeviceDetails);
		return updatedPeripheralDevice;
	}

	/**
	 * Delete a peripheral device with the specified UID
	 * 
	 * @param peripheralDeviceUID UID of the peripheral device
	 * @throws ResourceNotFoundException if there is no peripheral device with the
	 *                                   specified UID
	 * @return HttpStatus.OK if the peripheral device was successfully deleted
	 *
	 */
	public ResponseEntity<?> deletePeripheralDevice(Integer peripheralDeviceUID) {
		PeripheralD peripheralDevice = peripheralDRepository.findById(peripheralDeviceUID)
				.orElseThrow(() -> new ResourceNotFoundException("PeripheralDevice", "UID", peripheralDeviceUID));

		peripheralDRepository.delete(peripheralDevice);

		return ResponseEntity.ok().build();
	}

}
