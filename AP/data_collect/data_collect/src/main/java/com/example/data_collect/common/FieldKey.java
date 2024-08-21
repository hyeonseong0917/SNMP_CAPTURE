package com.example.data_collect.common;


public enum FieldKey {
    // Ip주소
    IP_AD_ENT_IF_INDEX("ipAdEntIfIndex"),
    // Ip주소와 연결된 인터페이스 인덱스
    IP_NET_TO_MEDIA_IF_INDEX("ipNetToMediaIfIndex"),
    // 인터페이스 인덱스
    IF_INDEX("ifIndex"),
    // 인터페이스 설명
    IF_DESCR("ifDescr"),
    // 인터페이스 관리자 운영 상태
    IF_ADMIN_STATUS("ifAdminStatus"),
    // 인터페이스가 수신한 오류 패킷 수
    IF_IN_ERROR_PACKET("ifInErrors"),
    // 인터페이스가 송신한 오류 패킷 수
    IF_OUT_ERROR_PACKET("ifOutErrors"),
    // 인터페이스가 송신한 패킷 수
    IF_OUT_OCTETS("ifOutOctets"),
    // 인터페이스가 수신한 패킷 수
    IF_IN_OCTETS("ifInOctets"),
    // 인터페이스 운영 상태
    IF_OPER_STATUS("ifOperStatus")
    ;

    private final String key;

    FieldKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
