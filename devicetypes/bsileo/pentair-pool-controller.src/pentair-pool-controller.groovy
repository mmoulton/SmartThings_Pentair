import groovy.util.Eval;

metadata {

	definition (name: "Pentair Pool Controller", namespace: "bsileo", author: "Brad Sileo") {
    capability "Polling"
    capability "Refresh"
    capability "Configuration"
    capability "Switch"
    capability "Actuator"
    capability "Sensor"
    attribute "poolPump","string"
    attribute "spaPump","string"
    attribute "valve","string"
    command "poolPumpOn"
    command "poolPumpOff"
    command "spaPumpOn"
    command "spaPumpOff"
  }

	preferences {
    section("Configuration") {
      input "mainSwitchMode", "enum", title: "Main Tile Mode", required:true,  displayDuringSetup: true , options: ["Pool Pump"], description:"Select what feature to control with the main tile"
    }
	}

	tiles(scale: 2) {

    standardTile("mainSwitch", "device.switch", height:1,width:1,inactiveLabel: false,canChangeIcon: true, decoration: "flat") {
        state "off", label: "off", icon: "st.Lighting.light1", backgroundColor: "#ffffff", action: "switch.on", nextState: "on"
        state "on", label: "on", icon: "st.Lighting.light1", backgroundColor: "#00a0dc", action: "switch.off", nextState: "off"
        state "updating", label:"Updating...", icon: "st.Lighting.light13"
    }

    childDeviceTile("poolTemp", "poolHeat", height: 2, width: 2, childTileName: "temperature")
    childDeviceTile("poolPump", "poolPump", height:1, width:2, childTileName: "switch")
    childDeviceTile("poolHeatmode", "poolHeat", height: 1, width: 2, childTileName: "mode")
    childDeviceTile("poolHeatlower", "poolHeat", height: 1, width: 1, childTileName: "lowerHeatingSetpoint")
    childDeviceTile("poolHeatset", "poolHeat", height: 1, width: 2, childTileName: "heatingSetpoint")
    childDeviceTile("poolHeatraise", "poolHeat", height: 1, width: 1, childTileName: "raiseHeatingSetpoint")

    childDeviceTile("spaTemp", "spaHeat", height: 2, width: 2, childTileName: "temperature")
    childDeviceTile("spaPump", "spaPump", height: 1, width: 2, childTileName: "switch")
    childDeviceTile("SpaHeatmode", "spaHeat", height: 1, width: 2, childTileName: "mode")
    childDeviceTile("SpaHeatlower", "spaHeat", height: 1, width: 1, childTileName: "lowerHeatingSetpoint")
    childDeviceTile("SpaHeatset", "spaHeat", height: 1, width: 2, childTileName: "heatingSetpoint")
    childDeviceTile("SpaHeatraise", "spaHeat", height: 1, width: 1, childTileName: "raiseHeatingSetpoint")

    // //Always SPA so do not display here (??)
    // // childDeviceTile("Aux 1 Switch", "circuit1", height:1,width:1,childTileName:"switch")
    childDeviceTile("Aux 2 Switch", "circuit2", height:1,width:1,childTileName:"switch")
    childDeviceTile("Aux 3 Switch", "circuit3", height:1,width:1,childTileName:"switch")
    childDeviceTile("Aux 4 Switch", "circuit4", height:1,width:1,childTileName:"switch")
    childDeviceTile("Aux 5 Switch", "circuit5", height:1,width:1,childTileName:"switch")
    // //Always Pool so do not display here (??)
    // //childDeviceTile("Aux 6 Switch", "circuit6", height:1,width:1,childTileName:"switch")
    childDeviceTile("Aux 7 Switch", "circuit7", height:1,width:1,childTileName:"switch")
    childDeviceTile("Aux 8 Switch", "circuit8", height:1,width:1,childTileName:"switch")
    for (i in 9..20) {
    	childDeviceTile("Aux ${i} Switch", "circuit${i}", height:1,width:1,childTileName:"switch")
    }

    childDeviceTile("airTemp", "airTemp", height: 1, width: 2, childTileName: "temperature")

    // Always have only one of the 2 below
    childDeviceTile("solarTemp", "solarTemp", height: 1, width: 2, childTileName: "temperature")
    childDeviceTile("solarDummy", "solarDummy", height: 1, width: 2, childTileName: "dummy")

    valueTile("valve", "valve", width: 1, height: 1, decoration: "flat") {
      state("valve", label:' Valve: ${currentValue}')
    }

    standardTile("refresh", "device.refresh", height: 1, width: 1, inactiveLabel: false) {
      state "default", label:'Refresh', action:"refresh.refresh",  icon:"st.secondary.refresh-icon"
    }

    section (hideable: true, hidden: true, "chlorinator") {
      childDeviceTile("saltPPM", "poolChlorinator", height: 2, width: 2, childTileName: "saltPPM")
      childDeviceTile("chlorinateSwitch", "poolChlorinator", height: 1, width: 1, childTileName: "chlorinate")
      childDeviceTile("currentOutput", "poolChlorinator", height: 1, width: 1, childTileName: "currentOutput")
      childDeviceTile("poolSpaSetpoint", "poolChlorinator", height: 1, width: 2, childTileName: "poolSpaSetpoint")
      childDeviceTile("superChlorinate", "poolChlorinator", height: 1, width: 1, childTileName: "superChlorinate")
      childDeviceTile("status", "poolChlorinator", height: 1, width: 3, childTileName: "status")
		}

		//KJC added intellichem section
    section (hideable:true, hidden:true, "intellichem") {
      childDeviceTile("ORP","poolIntellichem", height:2,width:2,childTileName:"ORP")
      childDeviceTile("modeORP","poolIntellichem", height:1,width:2,childTileName:"modeORP")
      childDeviceTile("tankORP","poolIntellichem", height:1,width:2,childTileName:"tankORP")

      childDeviceTile("ORPSetLower", "poolIntellichem", height:1,width:1,childTileName:"lowerORPSetpoint")
      childDeviceTile("setpointORP","poolIntellichem", height:1,width:2,childTileName:"setpointORP")
      childDeviceTile("ORPSetRaise", "poolIntellichem", height:1,width:1,childTileName:"raiseORPSetpoint")


      childDeviceTile("pH","poolIntellichem", height:2,width:2,childTileName:"pH")
      childDeviceTile("modepH","poolIntellichem", height:1,width:2,childTileName:"modepH")
      childDeviceTile("tankpH","poolIntellichem", height:1,width:2,childTileName:"tankpH")

      childDeviceTile("pHSetLower", "poolIntellichem", height:1,width:1,childTileName:"lowerpHSetpoint")
      childDeviceTile("setpointpH","poolIntellichem", height:1,width:2,childTileName:"setpointpH")
      childDeviceTile("pHSetRaise", "poolIntellichem", height:1,width:1,childTileName:"raisepHSetpoint")

      childDeviceTile("SI","poolIntellichem", height:2,width:2,childTileName:"SI")
      childDeviceTile("flowAlarm","poolIntellichem", height:1,width:2,childTileName:"flowAlarm")
      childDeviceTile("CYA","poolIntellichem", height:1,width:2,childTileName:"CYA")
      childDeviceTile("CALCIUMHARDNESS","poolIntellichem", height:1,width:2,childTileName:"CALCIUMHARDNESS")
      childDeviceTile("TOTALALKALINITY","poolIntellichem", height:1,width:2,childTileName:"TOTALALKALINITY")
    }

    // Aux Circuits must be manually adjusted for exclusion below since ST does not allow any dynamic processing.
    // The system will automatically include only the Aux circuits that exist so listing extras below will not impact the User Interface but
    // you must manually remove any entries that DO exist in your poolController configuration (e.g. poolcontroll:30000/config )
    // you do not want to see as the default configuration will display all circuits that exist

    main "mainSwitch"
    details "poolTemp","poolPump","PoolHeatmode","PoolHeatlower","PoolHeatset","PoolHeatraise",
            "spaTemp","spaPump","SpaHeatmode","SpaHeatlower","SpaHeatset","SpaHeatraise",
            "airTemp","solarTemp","solarDummy","valve","refresh",
            "saltPPM","chlorinateSwitch","currentOutput","poolSpaSetpoint","superChlorinate","status",
            "ORP","modeORP","tankORP","ORPSetLower","setpointORP","ORPSetRaise","pH","modepH","tankpH","pHSetLower","setpointpH","pHSetRaise","SI","flowAlarm","CYA","CALCIUMHARDNESS","TOTALALKALINITY",
            "Aux 2 Switch","Aux 3 Switch","Aux 4 Switch","Aux 5 Switch","Aux 7 Switch","Aux 8 Switch",
            "Aux 9 Switch","Aux 10 Switch","Aux 11 Switch","Aux 12 Switch","Aux 13 Switch","Aux 14 Switch",
            "Aux 15 Switch","Aux 16 Switch","Aux 17 Switch","Aux 18 Switch","Aux 19 Switch","Aux 20 Switch"
	}
}

def configure() {
  log.debug "Executing 'configure()'"
  updateDeviceNetworkID()
}

def installed() {
  init()
}

def updated() {
  init()
  if (!state.updatedLastRanAt || now() >= state.updatedLastRanAt + 5000) {
    state.updatedLastRanAt = now()
    log.debug "Executing 'updated()'"
    runIn(3, "updateDeviceNetworkID")
  } else {
    log.trace "updated(): Ran within last 5 seconds so aborting."
  }
}

def init() {
  getPoolPumpChild()
  getPoolHeatChild()
  getSpaPumpChild()
  getSpaHeatChild()
  getAirTempChild()
  getSolarTempChild()
  getPoolChlorinatorChild()
  getPoolIntellichemChild()
  getCircuitChildren()
}

def refresh() {
  log.info "Requested a refresh"
  poll()
}

def poll() {
  sendEthernet("/all")
}

// The primary entry point for device messages
def parse(String description) {
  def msg = parseLanMessage(description)
  log.debug "Parsing new message: ${msg}"

  if (msg.json.temperature != null) {
    parseTemps(msg.json.temperature)
  }

  if (msg.json.circuit != null) {
    parseCircuits(msg.json.circuit)
  }

  if (msg.json.time != null) {
  	log.info("Parse Time: ${msg}")
  }

  if (msg.json.schedule != null) {
  	log.info("Parse Schedule: ${msg}")
  }

  if (msg.json.pump != null) {
  	log.info("Parse Pump: ${msg}")
  }

  if (msg.json.valve != null) {
    log.info("Parse Valve: ${msg}")
    sendEvent(name: "valve", value: msg.valves)
  }

  if (msg.json.chlorinator != null) {
    parseChlorinator(msg.json.chlorinator)
  }

  if (msg.json.intellichem != null) {
    log.info("Parse Intellichem: ${msg}")
    getPoolIntellichemChild()?.parse(msg)
  }
}

def parseCircuits(msg) {
	log.info("Parse Circuits: ${msg}")
  msg.each {
    def currentID = toIntOrNull(it.key)
    def child = getChildCircuit(currentID)
    if (child) {
      def stat = it.value.status ? it.value.status : 0
      def status = stat == 0 ? "off" : "on"

      // Ensure the current state of the circuit is reflected in the device
      if (stat == 0) {
        child.offConfirmed()
      } else {
        child.onConfirmed()
      }

      // Ensure the pool pump device reflects the state of the actual pool circuit
      if (currentID == poolPumpCircuitId()) {
        sendEvent(name: "switch", value: status, displayed: true)
        sendEvent(name: "poolPump", value: status, displayed: true)
        def poolPump = getPoolPumpChild()
        if (stat == 0) {
          poolPump.offConfirmed()
        } else {
          poolPump.onConfirmed()
        }
      }

      // Ensure the spa pump device reflects the state of the actual spa circuit
      if (currentID == spaPumpCircuitId()) {
        sendEvent(name: "spaPump", value: status, displayed: true)

        def spaPump = getSpaPumpChild()
        if (stat == 0) {
          spaPump.offConfirmed()
        } else {
          spaPump.onConfirmed()
        }
      }

      child.setCircuitFunction("${it.value.circuitFunction}")
      child.setFriendlyName("${it.value.friendlyName}")
      child.setCircuitId(currentID)

      sendEvent(name: "circuit${currentID}", value: status,
                displayed: true, descriptionText:"Circuit ${child.label} set to ${status}")
    }
  }
}

def getChildCircuit(id) {
	def circuits = getCircuitChildren()
  return circuits[toIntOrNull(id)]
}

def getHubId() {
  return location.hubs[0]?.id
}

def getPoolPumpChild() {
  def child = childDevices.find({it.deviceNetworkId == getChildDNI("poolPump")})
  if (!child) {
      child = addChildDevice("bsileo","Pentair Pool Pump Control", getChildDNI("poolPump"), getHubId(),
                                [completedSetup: true, label: "${device.displayName} (Pool Pump)" , isComponent:false, componentName: "poolPump", componentLabel:"${device.displayName} (Pool Pump)" ])
      log.debug "Created PoolPump"
  }
	return child
}

def getPoolHeatChild() {
  def child = childDevices.find({it.deviceNetworkId == getChildDNI("poolHeat")})
  if (!child) {
    child = addChildDevice("bsileo","Pentair Water Thermostat", getChildDNI("poolHeat"), getHubId(),
                              [completedSetup: true, label: "${device.displayName} (Pool Heat)" , isComponent:false, componentName: "poolHeat", componentLabel:"${device.displayName} (Pool Heat)" ])
    log.debug "Created PoolHeat"
  }
	return child
}

def getSpaPumpChild() {
  def child
  if (getDataValue("includeSpa") == 'true') {
      child = childDevices.find({it.deviceNetworkId == getChildDNI("spaPump")})
      if (!child) {
          child = addChildDevice("bsileo","Pentair Spa Pump Control", getChildDNI("spaPump"), getHubId(),
                                    [completedSetup: true, label: "${device.displayName} (Spa Pump)" , isComponent:false, componentName: "spaPump", componentLabel:"${device.displayName} (Spa Pump)" ])
          log.debug "Created SpaPump Child"
      }
  }
	return child;
}

def getSpaHeatChild() {
  def child
  if (getDataValue("includeSpa") == 'true') {
    child = childDevices.find({it.deviceNetworkId == getChildDNI("spaHeat")})
    if (!child) {
        child = addChildDevice("bsileo","Pentair Water Thermostat", getChildDNI("spaHeat"), getHubId(),
                                  [completedSetup: true, label: "${device.displayName} (Spa Heat)" , isComponent:false, componentName: "spaHeat", componentLabel:"${device.displayName} (Spa Heat)" ])
        log.debug "Created SpaHeat"
    }
  }
	return child
}

def getAirTempChild() {
  def child = childDevices.find({it.deviceNetworkId == getChildDNI("airTemp")})
  if (!child) {
    child = addChildDevice("bsileo","Pentair Temperature Measurement Capability", getChildDNI("airTemp"), getHubId(),
                              [ label: "${device.displayName} Air Temperature", componentName: "airTemp", componentLabel: "${device.displayName} Air Temperature",
                              isComponent:false, completedSetup:true])
  }
	return child
}

def getSolarTempChild() {
  def child
  if (getDataValue("includeSolar") == 'true') {
    child = childDevices.find({it.deviceNetworkId == getChildDNI("solarTemp")})
    if (!child) {
      log.debug("Create Solar temp")
      child = addChildDevice("bsileo","Pentair Temperature Measurement Capability", getChildDNI("solarTemp"), getHubId(),
                                [ label: "${device.displayName} Solar Temperature", componentName: "solarTemp", componentLabel: "${device.displayName} Solar Temperature",
                                isComponent:false, completedSetup:true])
    }
  } else {
    child = childDevices.find({it.deviceNetworkId == getChildDNI("solarDummy")})
    if (!child) {
      log.debug("Create Solar Dummy")
      child = addChildDevice("bsileo","Pentair Dummy Tile", getChildDNI("solarDummy"), getHubId(),
                                [ label: "${device.displayName} Solar Dummy", componentName: "solarDummy", componentLabel: "${device.displayName} Solar Dummy",
                                isComponent:false, completedSetup:true])
      }
  }
	return child
}

def getPoolChlorinatorChild() {
  def child
  if (getDataValue("includeChlorinator")=='true') {
    child = childDevices.find({it.deviceNetworkId == getChildDNI("poolChlorinator")})
    if (!child) {
      log.debug("Create Chlorinator")
      child = addChildDevice("bsileo","Pentair Chlorinator", getChildDNI("poolChlorinator"), getHubId(),
                                      [ label: "${device.displayName} Chlorinator", componentName: "poolChlorinator", componentLabel: "${device.displayName} Chlorinator",
                                      isComponent:true, completedSetup:true])
    }
  }
	return child
}

def getPoolIntellichemChild() {
  def child
  if (getDataValue("includeIntellichem")=='true') {
    child = childDevices.find({it.deviceNetworkId == getChildDNI("poolIntellichem")})
    if (!child) {
      log.debug("Create Intellichem")
      child = addChildDevice("bsileo","Pentair Intellichem", getChildDNI("poolIntellichem"), getHubId(),
                                      [ label: "${device.displayName} Intellichem", componentName: "poolIntellichem", componentLabel: "${device.displayName} Intellichem",
                                      isComponent:false, completedSetup:true])
    }
  }
	return child
}

def getCircuitChildren() {
  def children = [:]

  def nLCircuits = parent.state.nonLightCircuits
  nLCircuits.each {i,k ->
    def cData = parent.state.circuitData[i.toString()]
    if (cData.friendlyName == "NOT USED") return
    if (cData.friendlyName.startsWith("AUX ")) return
    if (cData.friendlyName.startsWith("FEATURE ")) return

    def auxName = "circuit${i}"
    def auxLabel = "${device.displayName} (${cData.friendlyName})"
    try {
      def auxButton = childDevices.find({it.deviceNetworkId == getChildDNI(auxName)})
      if (!auxButton) {
        log.info "Create Aux Circuit switch ${auxLabel} Named=${auxName}"
        auxButton = addChildDevice("bsileo","Pentair Circuit Switch", getChildDNI(auxName), getHubId(),
                                    [completedSetup: true, label: auxLabel , isComponent:false, componentName: auxName, componentLabel: auxLabel,
                                    data: [type:cData.circuitFunction, raw: cData]])
        log.debug "Success - Created Circuit Switch ${i}"
      }
      children[toIntOrNull(i)] = auxButton
    } catch(physicalgraph.app.exception.UnknownDeviceTypeException e) {
      log.debug "Error! " + e
    }
  }

  def lCircuits = parent.state.lightCircuits
  lCircuits.each {i,k ->
    def auxName = "circuit${i}"
    def auxLabel = "${device.displayName} (${k.circuitName})"
    try {
      def auxButton = childDevices.find({it.deviceNetworkId == getChildDNI(auxName)})
      if (!auxButton) {
        log.info "Create Light switch ${auxLabel} Named=${auxName}"
          auxButton = addChildDevice("bsileo","Pentair Pool Light Switch", getChildDNI(auxName), getHubId(),
                                      [completedSetup: true, label: auxLabel , isComponent:false, componentName: auxName, componentLabel: auxLabel,
                                      data: [type:cData.circuitFunction, raw: cData]])
          log.debug "Success - Created Light switch ${i}"
      }
      children[toIntOrNull(i)] = auxButton
    } catch(physicalgraph.app.exception.UnknownDeviceTypeException e) {
      log.debug "Error! " + e
    }
  }

  return children
}

def getChildDNI(name) {
	return getDataValue("controllerMac") + "-" + name
}

def parseTemps(msg) {

  def ph = getPoolHeatChild()
  def sh = getSpaHeatChild()
  def at = getAirTempChild()
  def st = getSolarTempChild()

  msg.each {k, v ->
    //log.debug "TEMP Key:${k}  Val:${v}"
    switch (k) {
      case "poolTemp":
        ph?.setTemperature(v)
        break
      case "spaTemp":
        sh?.setTemperature(v)
        break
      case "airTemp":
        at?.setTemperature(v)
        break
      case "solarTemp":
        st?.setTemperature(v)
        break
      case "poolSetPoint":
        ph?.setHeatingSetpoint(v)
        break
      case "spaSetPoint":
        sp?.setHeatingSetpoint(v)
        break
      case "poolHeatMode":
        ph?.switchToModeID(v)
        break
      case "spaHeatMode":
        sh?.switchToModeID(v)
        break
      default:
        sendEvent(name: k, value: v, displayed:false)
        break
    }
	}
}

def parseChlorinator(msg) {
  def pc = getPoolChlorinatorChild()
  pc?.parse(msg)
}

def on() {
  poolPumpOn()
}

def off() {
  poolPumpOff()
}

def chlorinatorOn() {
  return chlorinatorOn(70)
}

def chlorinatorOn(level) {
  return sendEthernet("/chlorinator/${level}")
}

def chlorinatorOff() {
  return sendEthernet("/chlorinator/0")
}

def poolPumpOn() {
	return setCircuit(poolPumpCircuitId(),1)
}

def poolPumpOff() {
	return setCircuit(poolPumpCircuitId(),0)
}

def spaPumpOn() {
	log.debug "SpaPump ON"
	return setCircuit(spaPumpCircuitId(),1)
}

def spaPumpOff() {
	return setCircuit(spaPumpCircuitId(),0)
}

def laminarCircuitIds() {
	return circuitIdsOfType("MagicStream")
}

def lightCircuitIds() {
	return circuitIdsOfType("Intellibrite")
}

def poolPumpCircuitId() {
	return circuitIdOfType("Pool")
}

def spaPumpCircuitId() {
	return circuitIdOfType("Spa")
}

def circuitIdOfType(type) {
  return childDevices.find({it.currentCircuitFunction == type})?.currentCircuitId
}

def circuitIdsOfType(type) {
  return childDevices.findAll({it.currentCircuitFunction == type})?.collect({ it.currentCircuitId })
}

def childOn(id) {
	return setCircuit(id, 1)
}

def childOff(id) {
	return setCircuit(id, 0)
}

def setCircuit(circuit, state) {
  log.debug "Executing 'set(${circuit}, ${state})'"
  sendEthernet("/circuit/${circuit}/set/${state}")
}

// **********************************
// Heater control functions to update the current heater state / setpoints on the poolController.
// spdevice is the child device with the correct DNI to use in referecing SPA or POOL
// **********************************
def heaterOn(type) {
  sendEthernet("/${type}/mode/1")
}

def heaterOff(type) {
  sendEthernet("/${type}/mode/0")
}

def heaterSetMode(type, mode) {
  sendEthernet("/${type}/mode/${mode}", heaterModeCallback)
}

def updateSetpoint(type, setPoint) {
	sendEthernet("/${type}/setpoint/${setPoint}")
}

def heaterModeCallback(physicalgraph.device.HubResponse hubResponse) {
  log.debug "Entered heaterModeCallback()..."
	def msg = hubResponse.json
  if (msg.text.indexOf('spa') > 0) {
    def sh = getSpaHeatChild()
    log.info("Update Spa heater to ${msg.status}")
    sh?.switchToMode(msg.status)
  } else {
    def ph = getPoolHeatChild()
    log.info("Update Pool heater to ${msg.status}")
    ph?.switchToMode(msg.status)
  }
}

// INTERNAL Methods
private sendEthernet(message) {
	sendEthernet(message,null)
}

private sendEthernet(message, aCallback) {
  def ip = getDataValue('controllerIP')
  def port = getDataValue('controllerPort')
  //log.debug "Try for 'sendEthernet' http://${ip}:${port}${message}"
  if (ip != null && port != null) {
    log.info "SEND http://${ip}:${port}${message}"
    sendHubCommand(new physicalgraph.device.HubAction(
      [
        method: "GET",
        path: "${message}",
        //protocol: Protocol.LAN,
        headers: [
          HOST: "${ip}:${port}",
          "Accept":"application/json"
        ],
        query:"",
        body:""
      ],
      null,
      [
        callback:aCallback
        //type:LAN_TYPE_CLIENT,
        //protocol:LAN_PROTOCOL_TCP
      ]
    ))
  }
}

private updateDeviceNetworkID(){
  setDeviceNetworkId()
}

private setDeviceNetworkId(){
  def hex = getDataValue('controllerMac').toUpperCase().replaceAll(':', '')
  if (device.deviceNetworkId != "$hex") {
    device.deviceNetworkId = "$hex"
    log.debug "Device Network Id set to ${device.deviceNetworkId}"
  }
}

private String convertHostnameToIPAddress(hostname) {
  def params = [
    uri: "http://dns.google.com/resolve?name=" + hostname,
    contentType: 'application/json'
  ]

  def retVal = null

  try {
    retVal = httpGet(params) { response ->
      log.trace "Request was successful, data=$response.data, status=$response.status"
      //log.trace "Result Status : ${response.data?.Status}"
      if (response.data?.Status == 0) { // Success
        for (answer in response.data?.Answer) { // Loop through results looking for the first IP address returned otherwise it's redirects
          //log.trace "Processing response: ${answer}"
          if (isIPAddress(answer?.data)) {
            log.trace "Hostname ${answer?.name} has IP Address ${answer?.data}"
            return answer?.data // We're done here (if there are more ignore it, we'll use the first IP address returned)
          } else {
            log.trace "Hostname ${answer?.name} redirected to ${answer?.data}"
          }
        }
      } else {
        log.warn "DNS unable to resolve hostname ${response.data?.Question[0]?.name}, Error: ${response.data?.Comment}"
      }
    }
  } catch (Exception e) {
    log.warn("Unable to convert hostname to IP Address, Error: $e")
  }

  //log.trace "Returning IP $retVal for Hostname $hostname"
  return retVal
}

private getHostAddress() {
	return "${ip}:${port}"
}

// gets the address of the Hub
private getCallBackAddress() {
  return device.hub.getDataValue("localIP") + ":" + device.hub.getDataValue("localSrvPortTCP")
}


private String convertIPtoHex(ipAddress) {
  String hex = ipAddress.tokenize( '.' ).collect {  String.format( '%02x', it.toInteger() ) }.join()
  return hex
}

private String convertPortToHex(port) {
	String hexport = port.toString().format( '%04x', port.toInteger() )
  return hexport
}

// TEMPERATUE Functions
// Get stored temperature from currentState in current local scale

def getTempInLocalScale(state) {
	def temp = device.currentState(state)
	def scaledTemp = convertTemperatureIfNeeded(temp.value.toBigDecimal(), temp.unit).toDouble()
	return (getTemperatureScale() == "F" ? scaledTemp.round(0).toInteger() : roundC(scaledTemp))
}

// Get/Convert temperature to current local scale
def getTempInLocalScale(temp, scale) {
	def scaledTemp = convertTemperatureIfNeeded(temp.toBigDecimal(), scale).toDouble()
	return (getTemperatureScale() == "F" ? scaledTemp.round(0).toInteger() : roundC(scaledTemp))
}

// Get stored temperature from currentState in device scale
def getTempInDeviceScale(state) {
	def temp = device.currentState(state)
	if (temp && temp.value && temp.unit) {
		return getTempInDeviceScale(temp.value.toBigDecimal(), temp.unit)
	}
	return 0
}

def getTempInDeviceScale(temp, scale) {
	if (temp && scale) {
		//API return/expects temperature values in F
		return ("F" == scale) ? temp : celsiusToFahrenheit(temp).toDouble().round(0).toInteger()
	}
	return 0
}

def roundC (tempC) {
	return (Math.round(tempC.toDouble() * 2))/2
}

 def toIntOrNull(it) {
   return it?.isInteger() ? it.toInteger() : null
 }

def sync(ip, port) {
	def existingIp = getDataValue("controllerIP")
	def existingPort = getDataValue("controllerPort")
	if (ip && ip != existingIp) {
		updateDataValue("ControllerIP", ip)
	}
	if (port && port != existingPort) {
		updateDataValue("controllerPort", port)
	}
}
