
package br.ufsc.silq.config;

public enum EstradosEnum {
    C(0), B5(1), B4(2), B3(3), B2(4), B1(5), A2(6), A1(7);

    private Integer estratoNumber;

    public Integer getEstratoNumber() {
        return estratoNumber;
    }

    EstradosEnum(Integer estratoNumber) {
        this.estratoNumber = estratoNumber;
    }
}
