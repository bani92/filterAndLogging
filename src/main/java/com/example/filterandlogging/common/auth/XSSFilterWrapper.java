package com.example.filterandlogging.common.auth;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class XSSFilterWrapper extends HttpServletRequestWrapper {

    // XSS 문자열 제거 결과 요청 InputStream 의 byte array 객체
    private byte[] rawData;

    public XSSFilterWrapper(HttpServletRequest request) {
        super(request);
        try {
            // 생성자에서 초기화 메소드 호출
            if(!initXSSFilterWrapper(request)) {
                throw new Exception("XSS 필터 수행 객체 구축 초기화 수행 오류");
            }
        } catch (NullPointerException npe) {
            // 에러로그 처리
        } catch (Exception e) {
            // 에러로그 처리
        }

    }

    private Boolean initXSSFilterWrapper(HttpServletRequest httpServletRequest) {
        InputStream inputStream = null;

        try {
            inputStream = httpServletRequest.getInputStream();
            if(inputStream != null) {
                this.rawData = replaceXSS(inputStream.readAllBytes());
                return true;
            }
            return false;
        } catch (NullPointerException npe) {
            // 에러로그 처리
            return false;
        } catch (Exception e) {
            // 에러로그 처리
            return false;
        }

    }

    private byte[] replaceXSS(byte[] data) {
        String strData = new String(data);
        strData = strData.replaceAll("\\<", "&lt;")
                .replaceAll("\\>", "&gt;")
                .replaceAll("\\(", "$#40;")
                .replaceAll("\\)", "$#41;")
                .replaceAll("&amp;#41", "&#41;")
                .replaceAll("&amp;#40", "&#40;");
        return strData.getBytes();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if(this.rawData == null) {
            return super.getInputStream();
        }

        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.rawData);

        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
    }

    @Override
    public String getParameter(String name) {
        return replaceXSS(super.getParameter(name));
    }

    @Override
    public String getQueryString() {
        return replaceXSS(super.getQueryString());
    }

    private String replaceXSS(String value) {
        if(value != null) {
            value = value.replaceAll("\\<", "&lt;")
                    .replaceAll("\\>", "&gt;")
                    .replaceAll("\\(", "&#40;")
                    .replaceAll("\\)", "&#41;")
                    .replaceAll("&amp;#41;", "&#41;")
                    .replaceAll("&amp;#40;", "&#40;")
            ;
        }

        return value;
    }

}
