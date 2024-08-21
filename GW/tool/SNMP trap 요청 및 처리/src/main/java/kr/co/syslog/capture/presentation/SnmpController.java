package kr.co.syslog.capture.presentation;


import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class SnmpController {
    private static final String COMMUNITY = "codej";
    private static final int SNMP_VERSION = SnmpConstants.version2c;
    private static final String SNMP_ADDRESS = "udp:192.168.0.1/161";

    @GetMapping("/snmp/syslog")
    public String getSyslog(@RequestParam String oid) {
        StringBuilder result = new StringBuilder();
        Snmp snmp = null;
        TransportMapping transport = null;

        try {
            transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            transport.listen();

            Address targetAddress = GenericAddress.parse(SNMP_ADDRESS);
            CommunityTarget target = new CommunityTarget();
            target.setCommunity(new OctetString(COMMUNITY));
            target.setAddress(targetAddress);
            target.setVersion(SnmpConstants.version2c);
            target.setRetries(2);
            target.setTimeout(1500);

            PDU pdu = new PDU();
            pdu.add(new VariableBinding(new OID(oid)));
            pdu.setType(PDU.GET);

            List<String> resultList = new ArrayList<>();
            boolean finished = false;

            ResponseEvent response = snmp.send(pdu, target);
            PDU responsePDU = response.getResponse();

            VariableBinding vb = responsePDU.get(0);
            resultList.add(vb.getOid() + " = " + vb.toValueString());
            pdu.setRequestID(new Integer32(0));
            pdu.set(0, vb);


            result.append("SNMP 응답 성공:\n");
            result.append(resultList).append("\n");
            System.out.println(result);

        } catch (IOException e) {
            result.append("SNMP 통신 중 오류 발생: ").append(e.getMessage());
        } finally {
            if (snmp != null) {
                try {
                    snmp.close();
                } catch (IOException e) {
                    result.append("\nSNMP 연결 종료 중 오류 발생: ").append(e.getMessage());
                }
            }
            if (transport != null) {
                try {
                    transport.close();
                } catch (IOException e) {
                    result.append("\n전송 연결 종료 중 오류 발생: ").append(e.getMessage());
                }
            }
        }

        return result.toString();
    }

    @GetMapping("/snmp/mib")
    public List<String> getMibInfo(@RequestParam String oid) throws IOException {
        List<String> result = new ArrayList<>();

        TransportMapping<UdpAddress> transport = new DefaultUdpTransportMapping();
        Snmp snmp = new Snmp(transport);
        transport.listen();

        Address targetAddress = GenericAddress.parse(SNMP_ADDRESS);
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(COMMUNITY));
        target.setAddress(targetAddress);
        target.setVersion(SNMP_VERSION);
        target.setRetries(2);
        target.setTimeout(1500);

        PDU pdu = new PDU();
        pdu.add(new VariableBinding(new OID(oid)));
        pdu.setType(PDU.GETNEXT);

        boolean finished = false;
        while (!finished) {
            ResponseEvent response = snmp.send(pdu, target);
            PDU responsePDU = response.getResponse();

            if (responsePDU == null) {
                finished = true;
            } else {
                VariableBinding vb = responsePDU.get(0);
                if (vb.getOid().startsWith(new OID(oid))) {
                    result.add(vb.getOid() + " : " + vb.toValueString());
                    pdu.setRequestID(new Integer32(0));
                    pdu.set(0, vb);
                } else {
                    finished = true;
                }
            }
        }

        snmp.close();
        return result;
    }
}
