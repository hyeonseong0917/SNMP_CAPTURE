package kr.co.syslog.capture.application;
import kr.co.syslog.capture.presentation.TrapController;
import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.MessageDispatcher;
import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SnmpTrapListener implements CommandResponder {

    private Snmp snmp;
    private final TrapController trapController;

    public SnmpTrapListener(TrapController trapController) {
        this.trapController = trapController;
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            startTrapListener();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startTrapListener() throws IOException {
        Address listenAddress = GenericAddress.parse("udp:0.0.0.0/162");
        TransportMapping<UdpAddress> transport = new DefaultUdpTransportMapping((UdpAddress) listenAddress);
        MessageDispatcher mDispatcher = new MessageDispatcherImpl();
        mDispatcher.addMessageProcessingModel(new MPv1());
        mDispatcher.addMessageProcessingModel(new MPv2c());

        snmp = new Snmp(mDispatcher, transport);
        snmp.addCommandResponder(this);
        transport.listen();
    }

    @Override
    public void processPdu(CommandResponderEvent event) {
        String trapMessage = "Received SNMP Trap: " + event.getPDU();
        System.out.println(trapMessage);
        trapController.addTrap(trapMessage);
    }
}