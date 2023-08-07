package com.example.ipsapp

import com.example.ipsapp.fileExamples.file
import com.google.gson.Gson
import org.json.JSONObject
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
class QRDecoderTest {

    private val urlUtilsMock = mock(UrlUtils::class.java)

    private var url = "https://smart-health-links-ips.cirg.washington.edu/ips#shlink:/eyJ1cmwiOiJodHRwczovL3NtYXJ0LWhlYWx0aC1saW5rcy1zZXJ2ZXIuY2lyZy53YXNoaW5ndG9uLmVkdS9hcGkvc2hsL3NzcFhkSHdrbmRQb3Z2bmNpbW1MS2xaS2pMR3FiUzFzQUtycmNDbDUzRTQiLCJmbGFnIjoiIiwia2V5IjoieUQ2VTQ1RjU4ZzJXOTRSUzRZVklMS0hoS0xpZ1lNdkRSQi0xenNGdjdMTSIsImxhYmVsIjoiU0hMIGZyb20gMjAyMy0wNy0xNyJ9"
    private var extracted = "eyJ1cmwiOiJodHRwczovL3NtYXJ0LWhlYWx0aC1saW5rcy1zZXJ2ZXIuY2lyZy53YXNoaW5ndG9uLmVkdS9hcGkvc2hsL3NzcFhkSHdrbmRQb3Z2bmNpbW1MS2xaS2pMR3FiUzFzQUtycmNDbDUzRTQiLCJmbGFnIjoiIiwia2V5IjoieUQ2VTQ1RjU4ZzJXOTRSUzRZVklMS0hoS0xpZ1lNdkRSQi0xenNGdjdMTSIsImxhYmVsIjoiU0hMIGZyb20gMjAyMy0wNy0xNyJ9"
    private var decodedBase64 = "{\"url\":\"https://smart-health-links-server.cirg.washington.edu/api/shl/sspXdHwkndPovvncimmLKlZKjLGqbS1sAKrrcCl53E4\",\"flag\":\"\",\"key\":\"yD6U45F58g2W94RS4YVILKHhKLigYMvDRB-1zsFv7LM\",\"label\":\"SHL from 2023-07-17\"}"
    private val verifiableCredential = "{\"verifiableCredential\":[\"eyJ6aXAiOiJERUYiLCJhbGciOiJFUzI1NiIsImtpZCI6IjNLZmRnLVh3UC03Z1h5eXd0VWZVQUR3QnVtRE9QS01ReC1pRUxMMTFXOXMifQ.7V0Lc9u2sv4rGHV6pz01JIAk-PDNzYxj5-HGj8R20iY9nQ5IghITilRIyo6TyX-_C_ApWZIp2UnsTqapbZIgsFjs7vfhyc-9MMt6271Rnk-y7cEgmwivn415mo8Ej_JR3-Opnw3ERz6eRCIbQOqpSHtbvdgNetvUtJntWLam9R3L3Oqde73tz738ciJ62381ec5n91NxgeVF7--tnpcKX8R5yKPTqftOeLnMJRiF6WuRZmESg3xGn_QpFCvvPprGfiRkmlRkyTT1xJkqsVc-2OqFKrsgBEkhVXaZ5WIMz6dpvB2KPNhOA2_boJoGSc95NJXv2pZnGzql2NEEwQYzLMxt5mKdUaZ5jDuEmr0vW2Xlen7iTcdQCOSQh2OR5aAfuK0RTcfExMQ4o-Y2s7YJaEYjvxGyTQikhTfSS9ANVG8aRa_SqJRqOg39bdewPMcwbWzohosNjwrMeWDjwPE8PzCEozs25FFVeoECdpPxJMnCXOpsqwdC5VPZuEEY86hXyf655yV-GA-VGLVuZGNBW0VJGHv9JB1CckgmMzUJcyhmcMMPs0nEQf7eC56HUBd0Oh1D416ivUodX_4GHWVNK6YiEKmIpbTVSwPb1T3KHA87DjdB1VTDtm76WBMOcYUWWIbDpap9novFOq31yaf5KElVTdol1SoNTMd0bcqxTYiGIV-CbTvQMCfM0pnBtUAwkFk2Yh6JBRXjGUoCRMwBMQZSEKWWOAhLew1zqY0jqWyosDJVEKXKbCeKRDoMRYZ47KP9OE_gmoOIWa3ddRvDsC2TYW2mMWaL2fHPwWsEOhFcSZQVbZKLj6pBaqMYihiEyYWv8jqHOw_gF_o4juLs__5biXBxcdG_0KUMA-o4zuDjKB9H_-09fJBzNxLIi3imUvNJ-CJNJiLNL8_kE5nE4xMpwMNGvp05NTwYVGkeyADhw69U_lm-IuIHA7iQN06V2PXlLgg-TNLL-kZV2-YFAWqA5mleScbSQps8juNM5MXVQBY7qERwE_9SSoJCH6pWSw_Ct2XHXOOm60vjhTbBBvdNbIPT4sCyjMAAU7YNpYbcH8gfD5GU8FwgKMiv7179oWS5tnCNe64nNB8TDjZtUMfHDqMEW45uMdfSdV3zisIfgi270OrwGroI8xGaiDj0wigKY5Tl6dTLp6lQlsPBpF0QEvTGIzQW3ojHYTaWDlAoF_2SVVn9WlfjSr0eosPHe_u7O2f7x0fLKvvwSRqi33mMCEXKl-EfenW2izRCSfFO2SZFYwyUucFvMNGHMjY0gbTt94W6LluaGnRpJcjw2ny6KFx5WuP-h8IPPa4UdxBm-cY-TwF6CCYzPj-f9_f28ZY8ZejcRq17J-LDFEByucM3aZe5_EkyzUXzMBwu9-wdhQrCR3u8euWqhw8aq7q9ekqZRSnMV6jpqvo0QaMpoRQRLNUUps90LCjVwXhNAwPRCHDgA-sB-9Us4lTR4lHEvfdoNxkl2Qg9_pin4N5oJFIXYkI2nQALlPVrvH1HeX_j_McpJJxmc3HuIVrh8SZqufzyWui2sHQLfNdkgmJD-ODUnmZjz6GWIL5mcu5WtYAG9CG0IQDrnIcxeBuENw7xLvkELo1-GcvcJS1CkyIlBDQ3HTxEO0c7p2cnx2-PDxA9fIrOdh4dPD67-mz3SfXoyf7B4fHhs1cHe-qFImW7rLU1dTqN0SFPF2mKoc2DY0uhlZUOuhjGlei4KKMubTMXHqGJQOzxDWMjNRiE41ly2s74ewfGtjDXxIQIlYmzZZFhMYfpGuausY_dJPZV12HASCAcx5QEHaiz4QoLMNNycUACgEtbs5lOrlhF8zr3ue8LLjFWgFEx4mCXURvrvmdyHzqMrunP2cJeyIdxkuWhB1CRTaN8c4asE4cZcwx5Qfbf2y6uirSNjt1MpOfKs5bjxy5Us0V75av15as4bC6AvYh0kop8FmxOqjZDJzweiuW21Q1smnqU1cCBwVjgChtregCu6bgEqLJlgB3ZAYFYA50_XoNNSTdR3WP3EfDS_SyJoPQ2pRT5qGKt0OkNJQvNJdxORpdR4iWeN4XuDzBZ-LWMc6JnatjhEnk8TUORSmJ7eHK60ypGht494SFKELG2tXbotRaB1NXa04Dams8oFpJlGq7mYAiNHvZNRjjTbN9ielX7Z2KcDKPEhTrtUG_QXPbzJIdwAPcfRUniI_cSPXtxsNsS1Oqz1tXPC-oMcOUmQMulbYNWhwlkCBW2-qSVvIDlo-T8BjX2qA0dd82CqO9CK5tcYNf1AyxcQih3qUE5q2oMHeZcegjIhv56AQ0pDfFvWdFTkU6hq5GiF-A9Y96S8EgM-bUdJ2g6nqNjQHxZEXtbZ8tQc0XTeSbVLDBc0-MaNnzDwY6m-1gHNNNM0_Y1v2ZJj9epwQs1JvMNauAxwzCgg4IdV5NNETDMg4DjgHLNCagFsdeuarB7J2tgOcKwAgE0xPI86K55AfAIT47fmIQKm_qBV3O8nUfHqt96MkLDNJlO0F9yGOzvxm0a-YvrItUOvPDL3q9oskmd6NI6zbOxG2LBzL1Jkq7oUcwgwu1wgRYSDbrE8ytsoJ1Bl5C4MoMuEWa1BB08e7UEHRxrZQZd7HqOEz0DkEvSSxm0X0BvAu1HUSyyzYkRMHzDnhtGWFbG92ZHSpiWcKVcK0YMN6bQ3WiOFKiU5ziopcFdmHIVrg55FA5jSVvy6ThR7MNNhaxo0Wfcj4Mwgu52LjurReeVp9BHTcZ8Cx0dn7bCWSrGYSZnRua7jmej6bJO9sIAvKRaXRh8XS0RJxM-zUD3QTTNRlL6X4IwlqZZdapbaWTADsSYK0sIxwX_8wqWkrWquGiwtHv91uwar9v1-Rv-k666YBKn0-TGykmccgpCTWPJialuGY6B58vMzosps335qpwwA2_LX0185bXzcykG2zacPnGa-alKqN5PyXtn_PFJPDIjtmMlz0nvKwSEycMH7sOnVR7oCGg5L4ZIXGiySZmgmcyT97fBcLcRswzThqirN8m4V726jfJ0KponMR-X9w_lRCQHhn_A0ySTBKdKk4tIeMm4SPaAoxGYB9QAbm__plPLti1CbCJ9uX35YMBbgoI2_ErIwsSbZ24IJRehRj6mjqVhwjChrQr4PlhCViQ44GDlr-H_x1OIkRxREyx9L0khiTfKkQ5CoL1HwI0hXKUROFVWVGakQ4SLc1AGZAQXc0F3mIaF46bKn3Dtb1D-iYiKruconBQt0Dw7KlXYvnfWqKx9e6epxkzAUc__06TMJlA7hXUgl6QwQHo-lxYDMWEMgS9Khpf9UWQp05FJThXKDc51fJJEQt5Bh2fPvkCNxolUxIOBzLURp2zvQ2C1TcFzjatjxhjWCNFN1bqt66J5y_cAWAA4OBTpq5msYcTDDFFK0cElBGLTAS4CvSk1zzITieYiUHtu-q-5yekEbFvrU7NvG6RP-4DYtq33tb7RN_t6a7K6Nn81d1lYfm9bWv1WT1p7McPMx2GkRj9Kc4cMhpBQzlH2Cs30_lZzn0VDzoozGSWxaBXZNnu4Pc3kzRGoRIlQmL6calZ2Dwlqg5dxqLZ2OWVb2IcqDXrzaqXAImtXCwOK-dXa7uF1YO3QNd4tx3oKN1AcZ1pE9l7LJZRoXuEOZcxvTFzdWMGguttgQ7HAGBU4VK3QagSwwYX676z-ll1e1X-t1EanS-21pVhpufM6VYY8o9HCqHtfloNep0m2laB3dcatwr-OeW-Of7amLcA_2xZpEGg7VqS_Ov5zqH0__AOaFEtqWxFZhVA3iJ68UHXYqBpXRZSsSwbUarKiDKi1MKqvea0IVbFyuc5gej4IJ9mMCNAzinM8jd_HyUWMp-cYEqA4wcV1AJ11zKupbynNUYLUIyQfofrRFfEmBXeah_GfSk71j-Y6xPc1F2s-l0tsLAY\n"
    private val extractedCredential = "eyJ6aXAiOiJERUYiLCJhbGciOiJFUzI1NiIsImtpZCI6IjNLZmRnLVh3UC03Z1h5eXd0VWZVQUR3QnVtRE9QS01ReC1pRUxMMTFXOXMifQ.7V0Lc9u2sv4rGHV6pz01JIAk-PDNzYxj5-HGj8R20iY9nQ5IghITilRIyo6TyX-_C_ApWZIp2UnsTqapbZIgsFjs7vfhyc-9MMt6271Rnk-y7cEgmwivn415mo8Ej_JR3-Opnw3ERz6eRCIbQOqpSHtbvdgNetvUtJntWLam9R3L3Oqde73tz738ciJ62381ec5n91NxgeVF7--tnpcKX8R5yKPTqftOeLnMJRiF6WuRZmESg3xGn_QpFCvvPprGfiRkmlRkyTT1xJkqsVc-2OqFKrsgBEkhVXaZ5WIMz6dpvB2KPNhOA2_boJoGSc95NJXv2pZnGzql2NEEwQYzLMxt5mKdUaZ5jDuEmr0vW2Xlen7iTcdQCOSQh2OR5aAfuK0RTcfExMQ4o-Y2s7YJaEYjvxGyTQikhTfSS9ANVG8aRa_SqJRqOg39bdewPMcwbWzohosNjwrMeWDjwPE8PzCEozs25FFVeoECdpPxJMnCXOpsqwdC5VPZuEEY86hXyf655yV-GA-VGLVuZGNBW0VJGHv9JB1CckgmMzUJcyhmcMMPs0nEQf7eC56HUBd0Oh1D416ivUodX_4GHWVNK6YiEKmIpbTVSwPb1T3KHA87DjdB1VTDtm76WBMOcYUWWIbDpap9novFOq31yaf5KElVTdol1SoNTMd0bcqxTYiGIV-CbTvQMCfM0pnBtUAwkFk2Yh6JBRXjGUoCRMwBMQZSEKWWOAhLew1zqY0jqWyosDJVEKXKbCeKRDoMRYZ47KP9OE_gmoOIWa3ddRvDsC2TYW2mMWaL2fHPwWsEOhFcSZQVbZKLj6pBaqMYihiEyYWv8jqHOw_gF_o4juLs__5biXBxcdG_0KUMA-o4zuDjKB9H_-09fJBzNxLIi3imUvNJ-CJNJiLNL8_kE5nE4xMpwMNGvp05NTwYVGkeyADhw69U_lm-IuIHA7iQN06V2PXlLgg-TNLL-kZV2-YFAWqA5mleScbSQps8juNM5MXVQBY7qERwE_9SSoJCH6pWSw_Ct2XHXOOm60vjhTbBBvdNbIPT4sCyjMAAU7YNpYbcH8gfD5GU8FwgKMiv7179oWS5tnCNe64nNB8TDjZtUMfHDqMEW45uMdfSdV3zisIfgi270OrwGroI8xGaiDj0wigKY5Tl6dTLp6lQlsPBpF0QEvTGIzQW3ojHYTaWDlAoF_2SVVn9WlfjSr0eosPHe_u7O2f7x0fLKvvwSRqi33mMCEXKl-EfenW2izRCSfFO2SZFYwyUucFvMNGHMjY0gbTt94W6LluaGnRpJcjw2ny6KFx5WuP-h8IPPa4UdxBm-cY-TwF6CCYzPj-f9_f28ZY8ZejcRq17J-LDFEByucM3aZe5_EkyzUXzMBwu9-wdhQrCR3u8euWqhw8aq7q9ekqZRSnMV6jpqvo0QaMpoRQRLNUUps90LCjVwXhNAwPRCHDgA-sB-9Us4lTR4lHEvfdoNxkl2Qg9_pin4N5oJFIXYkI2nQALlPVrvH1HeX_j_McpJJxmc3HuIVrh8SZqufzyWui2sHQLfNdkgmJD-ODUnmZjz6GWIL5mcu5WtYAG9CG0IQDrnIcxeBuENw7xLvkELo1-GcvcJS1CkyIlBDQ3HTxEO0c7p2cnx2-PDxA9fIrOdh4dPD67-mz3SfXoyf7B4fHhs1cHe-qFImW7rLU1dTqN0SFPF2mKoc2DY0uhlZUOuhjGlei4KKMubTMXHqGJQOzxDWMjNRiE41ly2s74ewfGtjDXxIQIlYmzZZFhMYfpGuausY_dJPZV12HASCAcx5QEHaiz4QoLMNNycUACgEtbs5lOrlhF8zr3ue8LLjFWgFEx4mCXURvrvmdyHzqMrunP2cJeyIdxkuWhB1CRTaN8c4asE4cZcwx5Qfbf2y6uirSNjt1MpOfKs5bjxy5Us0V75av15as4bC6AvYh0kop8FmxOqjZDJzweiuW21Q1smnqU1cCBwVjgChtregCu6bgEqLJlgB3ZAYFYA50_XoNNSTdR3WP3EfDS_SyJoPQ2pRT5qGKt0OkNJQvNJdxORpdR4iWeN4XuDzBZ-LWMc6JnatjhEnk8TUORSmJ7eHK60ypGht494SFKELG2tXbotRaB1NXa04Dams8oFpJlGq7mYAiNHvZNRjjTbN9ielX7Z2KcDKPEhTrtUG_QXPbzJIdwAPcfRUniI_cSPXtxsNsS1Oqz1tXPC-oMcOUmQMulbYNWhwlkCBW2-qSVvIDlo-T8BjX2qA0dd82CqO9CK5tcYNf1AyxcQih3qUE5q2oMHeZcegjIhv56AQ0pDfFvWdFTkU6hq5GiF-A9Y96S8EgM-bUdJ2g6nqNjQHxZEXtbZ8tQc0XTeSbVLDBc0-MaNnzDwY6m-1gHNNNM0_Y1v2ZJj9epwQs1JvMNauAxwzCgg4IdV5NNETDMg4DjgHLNCagFsdeuarB7J2tgOcKwAgE0xPI86K55AfAIT47fmIQKm_qBV3O8nUfHqt96MkLDNJlO0F9yGOzvxm0a-YvrItUOvPDL3q9oskmd6NI6zbOxG2LBzL1Jkq7oUcwgwu1wgRYSDbrE8ytsoJ1Bl5C4MoMuEWa1BB08e7UEHRxrZQZd7HqOEz0DkEvSSxm0X0BvAu1HUSyyzYkRMHzDnhtGWFbG92ZHSpiWcKVcK0YMN6bQ3WiOFKiU5ziopcFdmHIVrg55FA5jSVvy6ThR7MNNhaxo0Wfcj4Mwgu52LjurReeVp9BHTcZ8Cx0dn7bCWSrGYSZnRua7jmej6bJO9sIAvKRaXRh8XS0RJxM-zUD3QTTNRlL6X4IwlqZZdapbaWTADsSYK0sIxwX_8wqWkrWquGiwtHv91uwar9v1-Rv-k666YBKn0-TGykmccgpCTWPJialuGY6B58vMzosps335qpwwA2_LX0185bXzcykG2zacPnGa-alKqN5PyXtn_PFJPDIjtmMlz0nvKwSEycMH7sOnVR7oCGg5L4ZIXGiySZmgmcyT97fBcLcRswzThqirN8m4V726jfJ0KponMR-X9w_lRCQHhn_A0ySTBKdKk4tIeMm4SPaAoxGYB9QAbm__plPLti1CbCJ9uX35YMBbgoI2_ErIwsSbZ24IJRehRj6mjqVhwjChrQr4PlhCViQ44GDlr-H_x1OIkRxREyx9L0khiTfKkQ5CoL1HwI0hXKUROFVWVGakQ4SLc1AGZAQXc0F3mIaF46bKn3Dtb1D-iYiKruconBQt0Dw7KlXYvnfWqKx9e6epxkzAUc__06TMJlA7hXUgl6QwQHo-lxYDMWEMgS9Khpf9UWQp05FJThXKDc51fJJEQt5Bh2fPvkCNxolUxIOBzLURp2zvQ2C1TcFzjatjxhjWCNFN1bqt66J5y_cAWAA4OBTpq5msYcTDDFFK0cElBGLTAS4CvSk1zzITieYiUHtu-q-5yekEbFvrU7NvG6RP-4DYtq33tb7RN_t6a7K6Nn81d1lYfm9bWv1WT1p7McPMx2GkRj9Kc4cMhpBQzlH2Cs30_lZzn0VDzoozGSWxaBXZNnu4Pc3kzRGoRIlQmL6calZ2Dwlqg5dxqLZ2OWVb2IcqDXrzaqXAImtXCwOK-dXa7uF1YO3QNd4tx3oKN1AcZ1pE9l7LJZRoXuEOZcxvTFzdWMGguttgQ7HAGBU4VK3QagSwwYX676z-ll1e1X-t1EanS-21pVhpufM6VYY8o9HCqHtfloNep0m2laB3dcatwr-OeW-Of7amLcA_2xZpEGg7VqS_Ov5zqH0__AOaFEtqWxFZhVA3iJ68UHXYqBpXRZSsSwbUarKiDKi1MKqvea0IVbFyuc5gej4IJ9mMCNAzinM8jd_HyUWMp-cYEqA4wcV1AJ11zKupbynNUYLUIyQfofrRFfEmBXeah_GfSk71j-Y6xPc1F2s-l0tsLAY9OtfBXGdMEMuQfTEo7lQIxNXo2S_Vo\n"

    @Test
    fun substringSuccessfullyExtractsUrl() {
        Assert.assertEquals(urlUtilsMock.extractUrl(url), extracted)
    }

    @Test
    fun decodeFunctionProperlyDecodesUrl() {
        val decodedWithFunction = String(urlUtilsMock.decodeUrl(urlUtilsMock.extractUrl(url)))
        Assert.assertEquals(decodedWithFunction, decodedBase64)
    }

    @Test
    fun extractVerifiableCredentialCorrectlyExtractsAToken() {
        // val extractedVCWithFunc = urlUtilsMock.extractVerifiableCredential(verifiableCredential)
        // Assert.assertEquals(extractedVCWithFunc, extractedCredential)
    }

    @Test
    fun jwtsCanBeDecodedAndDecompressedIntoData() {
        val jwt = "eyJ6aXAiOiJERUYiLCJhbGciOiJFUzI1NiIsImtpZCI6IjNLZmRnLVh3UC03Z1h5eXd0VWZVQUR3QnVtRE9QS01ReC1pRUxMMTFXOXMifQ.pZJJT8MwEIX_ChquaZZSthyBAxwQSCwX1IPrTBsjL9HYKRSU_85MaAVCiAtSDnH85vN7z3kHEyPU0KbUxbooYoc6j05RalHZ1OZaURMLfFWusxgLVvdIkIFfLKGujman5bQ8mM7y6dFhBmsN9TukTYdQP30xf-L2PxcTWTDq_zrjXO_Nm0omeJhnoAkb9Mkoe9cvnlEnsbVsDT0iRdHUMMvLvGKofD3rfWNRNIQx9KTxfowA241sGwl0sJZpQsiAD6AN52Ryb-0DWRbs5uuSBbvFL-Bbtsrz0qNy-AlRztiNHErhRfgrs0YvPd5YfiOYD5xsYTj6hUoCmZbV8aSsJuUMhiH71Ub1t42r771lEJNKfRxzym0nlNbXSmvj8Tw0I0GHxvjV6DhuYkK3_Xn4Xlp7nAdaFVJpEU1T6PUrA_Q4CeUJDPMhg24bfXSzREIv1r43x6KgdU_jlmS9N-5HXsYgLQM57kWsKJ0CCbIxsbNKarxGMglp7zLEziRluaP5-AzDBw.xOwN6qSTeHU-FkqTIojbvryr8Ztue_HBbiiGdIcfio7m2-STuC-CdNIEt9WbxU_CpveZwdwdYlaQ3cX-yi-SQg"
        val expectedData = "{\"iss\":\"https://spec.smarthealth.cards/examples/issuer\",\"nbf\":1649020324.265,\"vc\":{\"type\":[\"https://smarthealth.cards#health-card\",\"https://smarthealth.cards#health-card\",\"https://smarthealth.cards#immunization\"],\"credentialSubject\":{\"fhirVersion\":\"4.0.1\",\"fhirBundle\":{\"resourceType\":\"Bundle\",\"type\":\"collection\",\"entry\":[{\"fullUrl\":\"resource:0\",\"resource\":{\"resourceType\":\"Patient\",\"name\":[{\"family\":\"Brown\",\"given\":[\"Oliver\"]}],\"birthDate\":\"2017-01-04\"}},{\"fullUrl\":\"resource:1\",\"resource\":{\"resourceType\":\"Immunization\",\"status\":\"completed\",\"vaccineCode\":{\"coding\":[{\"system\":\"http://hl7.org/fhir/sid/cvx\",\"code\":\"08\"}]},\"patient\":{\"reference\":\"resource:0\"},\"occurrenceDateTime\":\"2017-01-04\",\"performer\":[{\"actor\":{\"display\":\"Meriter Hospital\"}}]}}]}}}}\n"
        val decoded = urlUtilsMock.decodeAndDecompressPayload(jwt)
        Assert.assertEquals(decoded.trim(), expectedData.trim())
    }

    @Test
    fun encodeAndCompress() {
        // println(urlUtilsMock.encodeAndCompressPayload(file, encryptionKey))
    }

    @Test
    fun testManifestPost() {
        val res = urlUtilsMock.getManifestUrl()
        println(res)
    }

    @Test
    fun canEncodeFhirResources() {
        val encryptionKey = "VW5kZXJzdGFuZGFibHktU2FsdHktUGFzc2FnZS0wOTY"//urlUtilsMock.generateRandomKey()
        // Log.d("enc key", encryptionKey)
        println(encryptionKey)

        // need to encode and compress the payload
        // val encodedPayload = urlUtils.encodeAndCompressPayload(file, encryptionKey)

        Gson().toJson(file)
        val contentEncrypted = urlUtilsMock.encrypt(file, encryptionKey)
        println(contentEncrypted)
        println(urlUtilsMock.decodeShc(contentEncrypted, "VW5kZXJzdGFuZGFibHktU2FsdHktUGFzc2FnZS0wOTY"))
    }

    @Test
    fun canGenerateManifestUrl() {
        val encryptionKey = urlUtilsMock.generateRandomKey()
        // Log.d("enc key", encryptionKey)
        println(encryptionKey)

        // need to encode and compress the payload
        // val encodedPayload = urlUtils.encodeAndCompressPayload(file, encryptionKey)

        val contentJson = Gson().toJson(file)
        val contentEncrypted = urlUtilsMock.encrypt(contentJson, encryptionKey)
        println(contentEncrypted)
    }

    @Test
    fun canDecodeJWE() {
        val jwe = "eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiZGlyIn0..OgGwTWbECJk9tQc4.PUxr0STCtKQ6DmdPqPtJtTowTBxdprFykeZ2WUOUw234_TtdGWLJ0hzfuWjZXDyBpa55TXwvSwobpcbut9Cdl2nATA0_j1nW0-A32uAwH0qEE1ELV5G0IQVT5AqKJRTCMGpy0mWH.qATmrk-UdwCOaT1TY6GEJg"
        println(urlUtilsMock.decodeShc(jwe, "VmFndWVseS1FbmdhZ2luZy1QYXJhZG94LTA1NTktMDg"))
    }

    @Test
    fun encryptWith128() {
        urlUtilsMock.generateRandomKey()
        val a = urlUtilsMock.encrypt(Gson().toJson(file), "VmFndWVseS1FbmdhZ2luZy1QYXJhZG94LTA1NTktMDg")

        println(urlUtilsMock.decodeShc(a, "VmFndWVseS1FbmdhZ2luZy1QYXJhZG94LTA1NTktMDg"))
    }


}