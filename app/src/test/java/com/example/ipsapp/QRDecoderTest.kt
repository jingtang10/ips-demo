package com.example.ipsapp

import com.google.gson.Gson
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.mock
class QRDecoderTest {

    private val urlUtilsMock = mock(UrlUtils::class.java)

    private var url = "https://smart-health-links-ips.cirg.washington.edu/ips#shlink:/eyJ1cmwiOiJodHRwczovL3NtYXJ0LWhlYWx0aC1saW5rcy1zZXJ2ZXIuY2lyZy53YXNoaW5ndG9uLmVkdS9hcGkvc2hsL3NzcFhkSHdrbmRQb3Z2bmNpbW1MS2xaS2pMR3FiUzFzQUtycmNDbDUzRTQiLCJmbGFnIjoiIiwia2V5IjoieUQ2VTQ1RjU4ZzJXOTRSUzRZVklMS0hoS0xpZ1lNdkRSQi0xenNGdjdMTSIsImxhYmVsIjoiU0hMIGZyb20gMjAyMy0wNy0xNyJ9"
    private var extracted = "eyJ1cmwiOiJodHRwczovL3NtYXJ0LWhlYWx0aC1saW5rcy1zZXJ2ZXIuY2lyZy53YXNoaW5ndG9uLmVkdS9hcGkvc2hsL3NzcFhkSHdrbmRQb3Z2bmNpbW1MS2xaS2pMR3FiUzFzQUtycmNDbDUzRTQiLCJmbGFnIjoiIiwia2V5IjoieUQ2VTQ1RjU4ZzJXOTRSUzRZVklMS0hoS0xpZ1lNdkRSQi0xenNGdjdMTSIsImxhYmVsIjoiU0hMIGZyb20gMjAyMy0wNy0xNyJ9"
    private var decodedBase64 = "{\"url\":\"https://smart-health-links-server.cirg.washington.edu/api/shl/sspXdHwkndPovvncimmLKlZKjLGqbS1sAKrrcCl53E4\",\"flag\":\"\",\"key\":\"yD6U45F58g2W94RS4YVILKHhKLigYMvDRB-1zsFv7LM\",\"label\":\"SHL from 2023-07-17\"}"
    private val verifiableCredential = "{\"verifiableCredential\":[\"eyJ6aXAiOiJERUYiLCJhbGciOiJFUzI1NiIsImtpZCI6IjNLZmRnLVh3UC03Z1h5eXd0VWZVQUR3QnVtRE9QS01ReC1pRUxMMTFXOXMifQ.7V0Lc9u2sv4rGHV6pz01JIAk-PDNzYxj5-HGj8R20iY9nQ5IghITilRIyo6TyX-_C_ApWZIp2UnsTqapbZIgsFjs7vfhyc-9MMt6271Rnk-y7cEgmwivn415mo8Ej_JR3-Opnw3ERz6eRCIbQOqpSHtbvdgNetvUtJntWLam9R3L3Oqde73tz738ciJ62381ec5n91NxgeVF7--tnpcKX8R5yKPTqftOeLnMJRiF6WuRZmESg3xGn_QpFCvvPprGfiRkmlRkyTT1xJkqsVc-2OqFKrsgBEkhVXaZ5WIMz6dpvB2KPNhOA2_boJoGSc95NJXv2pZnGzql2NEEwQYzLMxt5mKdUaZ5jDuEmr0vW2Xlen7iTcdQCOSQh2OR5aAfuK0RTcfExMQ4o-Y2s7YJaEYjvxGyTQikhTfSS9ANVG8aRa_SqJRqOg39bdewPMcwbWzohosNjwrMeWDjwPE8PzCEozs25FFVeoECdpPxJMnCXOpsqwdC5VPZuEEY86hXyf655yV-GA-VGLVuZGNBW0VJGHv9JB1CckgmMzUJcyhmcMMPs0nEQf7eC56HUBd0Oh1D416ivUodX_4GHWVNK6YiEKmIpbTVSwPb1T3KHA87DjdB1VTDtm76WBMOcYUWWIbDpap9novFOq31yaf5KElVTdol1SoNTMd0bcqxTYiGIV-CbTvQMCfM0pnBtUAwkFk2Yh6JBRXjGUoCRMwBMQZSEKWWOAhLew1zqY0jqWyosDJVEKXKbCeKRDoMRYZ47KP9OE_gmoOIWa3ddRvDsC2TYW2mMWaL2fHPwWsEOhFcSZQVbZKLj6pBaqMYihiEyYWv8jqHOw_gF_o4juLs__5biXBxcdG_0KUMA-o4zuDjKB9H_-09fJBzNxLIi3imUvNJ-CJNJiLNL8_kE5nE4xMpwMNGvp05NTwYVGkeyADhw69U_lm-IuIHA7iQN06V2PXlLgg-TNLL-kZV2-YFAWqA5mleScbSQps8juNM5MXVQBY7qERwE_9SSoJCH6pWSw_Ct2XHXOOm60vjhTbBBvdNbIPT4sCyjMAAU7YNpYbcH8gfD5GU8FwgKMiv7179oWS5tnCNe64nNB8TDjZtUMfHDqMEW45uMdfSdV3zisIfgi270OrwGroI8xGaiDj0wigKY5Tl6dTLp6lQlsPBpF0QEvTGIzQW3ojHYTaWDlAoF_2SVVn9WlfjSr0eosPHe_u7O2f7x0fLKvvwSRqi33mMCEXKl-EfenW2izRCSfFO2SZFYwyUucFvMNGHMjY0gbTt94W6LluaGnRpJcjw2ny6KFx5WuP-h8IPPa4UdxBm-cY-TwF6CCYzPj-f9_f28ZY8ZejcRq17J-LDFEByucM3aZe5_EkyzUXzMBwu9-wdhQrCR3u8euWqhw8aq7q9ekqZRSnMV6jpqvo0QaMpoRQRLNUUps90LCjVwXhNAwPRCHDgA-sB-9Us4lTR4lHEvfdoNxkl2Qg9_pin4N5oJFIXYkI2nQALlPVrvH1HeX_j_McpJJxmc3HuIVrh8SZqufzyWui2sHQLfNdkgmJD-ODUnmZjz6GWIL5mcu5WtYAG9CG0IQDrnIcxeBuENw7xLvkELo1-GcvcJS1CkyIlBDQ3HTxEO0c7p2cnx2-PDxA9fIrOdh4dPD67-mz3SfXoyf7B4fHhs1cHe-qFImW7rLU1dTqN0SFPF2mKoc2DY0uhlZUOuhjGlei4KKMubTMXHqGJQOzxDWMjNRiE41ly2s74ewfGtjDXxIQIlYmzZZFhMYfpGuausY_dJPZV12HASCAcx5QEHaiz4QoLMNNycUACgEtbs5lOrlhF8zr3ue8LLjFWgFEx4mCXURvrvmdyHzqMrunP2cJeyIdxkuWhB1CRTaN8c4asE4cZcwx5Qfbf2y6uirSNjt1MpOfKs5bjxy5Us0V75av15as4bC6AvYh0kop8FmxOqjZDJzweiuW21Q1smnqU1cCBwVjgChtregCu6bgEqLJlgB3ZAYFYA50_XoNNSTdR3WP3EfDS_SyJoPQ2pRT5qGKt0OkNJQvNJdxORpdR4iWeN4XuDzBZ-LWMc6JnatjhEnk8TUORSmJ7eHK60ypGht494SFKELG2tXbotRaB1NXa04Dams8oFpJlGq7mYAiNHvZNRjjTbN9ielX7Z2KcDKPEhTrtUG_QXPbzJIdwAPcfRUniI_cSPXtxsNsS1Oqz1tXPC-oMcOUmQMulbYNWhwlkCBW2-qSVvIDlo-T8BjX2qA0dd82CqO9CK5tcYNf1AyxcQih3qUE5q2oMHeZcegjIhv56AQ0pDfFvWdFTkU6hq5GiF-A9Y96S8EgM-bUdJ2g6nqNjQHxZEXtbZ8tQc0XTeSbVLDBc0-MaNnzDwY6m-1gHNNNM0_Y1v2ZJj9epwQs1JvMNauAxwzCgg4IdV5NNETDMg4DjgHLNCagFsdeuarB7J2tgOcKwAgE0xPI86K55AfAIT47fmIQKm_qBV3O8nUfHqt96MkLDNJlO0F9yGOzvxm0a-YvrItUOvPDL3q9oskmd6NI6zbOxG2LBzL1Jkq7oUcwgwu1wgRYSDbrE8ytsoJ1Bl5C4MoMuEWa1BB08e7UEHRxrZQZd7HqOEz0DkEvSSxm0X0BvAu1HUSyyzYkRMHzDnhtGWFbG92ZHSpiWcKVcK0YMN6bQ3WiOFKiU5ziopcFdmHIVrg55FA5jSVvy6ThR7MNNhaxo0Wfcj4Mwgu52LjurReeVp9BHTcZ8Cx0dn7bCWSrGYSZnRua7jmej6bJO9sIAvKRaXRh8XS0RJxM-zUD3QTTNRlL6X4IwlqZZdapbaWTADsSYK0sIxwX_8wqWkrWquGiwtHv91uwar9v1-Rv-k666YBKn0-TGykmccgpCTWPJialuGY6B58vMzosps335qpwwA2_LX0185bXzcykG2zacPnGa-alKqN5PyXtn_PFJPDIjtmMlz0nvKwSEycMH7sOnVR7oCGg5L4ZIXGiySZmgmcyT97fBcLcRswzThqirN8m4V726jfJ0KponMR-X9w_lRCQHhn_A0ySTBKdKk4tIeMm4SPaAoxGYB9QAbm__plPLti1CbCJ9uX35YMBbgoI2_ErIwsSbZ24IJRehRj6mjqVhwjChrQr4PlhCViQ44GDlr-H_x1OIkRxREyx9L0khiTfKkQ5CoL1HwI0hXKUROFVWVGakQ4SLc1AGZAQXc0F3mIaF46bKn3Dtb1D-iYiKruconBQt0Dw7KlXYvnfWqKx9e6epxkzAUc__06TMJlA7hXUgl6QwQHo-lxYDMWEMgS9Khpf9UWQp05FJThXKDc51fJJEQt5Bh2fPvkCNxolUxIOBzLURp2zvQ2C1TcFzjatjxhjWCNFN1bqt66J5y_cAWAA4OBTpq5msYcTDDFFK0cElBGLTAS4CvSk1zzITieYiUHtu-q-5yekEbFvrU7NvG6RP-4DYtq33tb7RN_t6a7K6Nn81d1lYfm9bWv1WT1p7McPMx2GkRj9Kc4cMhpBQzlH2Cs30_lZzn0VDzoozGSWxaBXZNnu4Pc3kzRGoRIlQmL6calZ2Dwlqg5dxqLZ2OWVb2IcqDXrzaqXAImtXCwOK-dXa7uF1YO3QNd4tx3oKN1AcZ1pE9l7LJZRoXuEOZcxvTFzdWMGguttgQ7HAGBU4VK3QagSwwYX676z-ll1e1X-t1EanS-21pVhpufM6VYY8o9HCqHtfloNep0m2laB3dcatwr-OeW-Of7amLcA_2xZpEGg7VqS_Ov5zqH0__AOaFEtqWxFZhVA3iJ68UHXYqBpXRZSsSwbUarKiDKi1MKqvea0IVbFyuc5gej4IJ9mMCNAzinM8jd_HyUWMp-cYEqA4wcV1AJ11zKupbynNUYLUIyQfofrRFfEmBXeah_GfSk71j-Y6xPc1F2s-l0tsLAY\n"
    private val extractedCredential = "eyJ6aXAiOiJERUYiLCJhbGciOiJFUzI1NiIsImtpZCI6IjNLZmRnLVh3UC03Z1h5eXd0VWZVQUR3QnVtRE9QS01ReC1pRUxMMTFXOXMifQ.7V0Lc9u2sv4rGHV6pz01JIAk-PDNzYxj5-HGj8R20iY9nQ5IghITilRIyo6TyX-_C_ApWZIp2UnsTqapbZIgsFjs7vfhyc-9MMt6271Rnk-y7cEgmwivn415mo8Ej_JR3-Opnw3ERz6eRCIbQOqpSHtbvdgNetvUtJntWLam9R3L3Oqde73tz738ciJ62381ec5n91NxgeVF7--tnpcKX8R5yKPTqftOeLnMJRiF6WuRZmESg3xGn_QpFCvvPprGfiRkmlRkyTT1xJkqsVc-2OqFKrsgBEkhVXaZ5WIMz6dpvB2KPNhOA2_boJoGSc95NJXv2pZnGzql2NEEwQYzLMxt5mKdUaZ5jDuEmr0vW2Xlen7iTcdQCOSQh2OR5aAfuK0RTcfExMQ4o-Y2s7YJaEYjvxGyTQikhTfSS9ANVG8aRa_SqJRqOg39bdewPMcwbWzohosNjwrMeWDjwPE8PzCEozs25FFVeoECdpPxJMnCXOpsqwdC5VPZuEEY86hXyf655yV-GA-VGLVuZGNBW0VJGHv9JB1CckgmMzUJcyhmcMMPs0nEQf7eC56HUBd0Oh1D416ivUodX_4GHWVNK6YiEKmIpbTVSwPb1T3KHA87DjdB1VTDtm76WBMOcYUWWIbDpap9novFOq31yaf5KElVTdol1SoNTMd0bcqxTYiGIV-CbTvQMCfM0pnBtUAwkFk2Yh6JBRXjGUoCRMwBMQZSEKWWOAhLew1zqY0jqWyosDJVEKXKbCeKRDoMRYZ47KP9OE_gmoOIWa3ddRvDsC2TYW2mMWaL2fHPwWsEOhFcSZQVbZKLj6pBaqMYihiEyYWv8jqHOw_gF_o4juLs__5biXBxcdG_0KUMA-o4zuDjKB9H_-09fJBzNxLIi3imUvNJ-CJNJiLNL8_kE5nE4xMpwMNGvp05NTwYVGkeyADhw69U_lm-IuIHA7iQN06V2PXlLgg-TNLL-kZV2-YFAWqA5mleScbSQps8juNM5MXVQBY7qERwE_9SSoJCH6pWSw_Ct2XHXOOm60vjhTbBBvdNbIPT4sCyjMAAU7YNpYbcH8gfD5GU8FwgKMiv7179oWS5tnCNe64nNB8TDjZtUMfHDqMEW45uMdfSdV3zisIfgi270OrwGroI8xGaiDj0wigKY5Tl6dTLp6lQlsPBpF0QEvTGIzQW3ojHYTaWDlAoF_2SVVn9WlfjSr0eosPHe_u7O2f7x0fLKvvwSRqi33mMCEXKl-EfenW2izRCSfFO2SZFYwyUucFvMNGHMjY0gbTt94W6LluaGnRpJcjw2ny6KFx5WuP-h8IPPa4UdxBm-cY-TwF6CCYzPj-f9_f28ZY8ZejcRq17J-LDFEByucM3aZe5_EkyzUXzMBwu9-wdhQrCR3u8euWqhw8aq7q9ekqZRSnMV6jpqvo0QaMpoRQRLNUUps90LCjVwXhNAwPRCHDgA-sB-9Us4lTR4lHEvfdoNxkl2Qg9_pin4N5oJFIXYkI2nQALlPVrvH1HeX_j_McpJJxmc3HuIVrh8SZqufzyWui2sHQLfNdkgmJD-ODUnmZjz6GWIL5mcu5WtYAG9CG0IQDrnIcxeBuENw7xLvkELo1-GcvcJS1CkyIlBDQ3HTxEO0c7p2cnx2-PDxA9fIrOdh4dPD67-mz3SfXoyf7B4fHhs1cHe-qFImW7rLU1dTqN0SFPF2mKoc2DY0uhlZUOuhjGlei4KKMubTMXHqGJQOzxDWMjNRiE41ly2s74ewfGtjDXxIQIlYmzZZFhMYfpGuausY_dJPZV12HASCAcx5QEHaiz4QoLMNNycUACgEtbs5lOrlhF8zr3ue8LLjFWgFEx4mCXURvrvmdyHzqMrunP2cJeyIdxkuWhB1CRTaN8c4asE4cZcwx5Qfbf2y6uirSNjt1MpOfKs5bjxy5Us0V75av15as4bC6AvYh0kop8FmxOqjZDJzweiuW21Q1smnqU1cCBwVjgChtregCu6bgEqLJlgB3ZAYFYA50_XoNNSTdR3WP3EfDS_SyJoPQ2pRT5qGKt0OkNJQvNJdxORpdR4iWeN4XuDzBZ-LWMc6JnatjhEnk8TUORSmJ7eHK60ypGht494SFKELG2tXbotRaB1NXa04Dams8oFpJlGq7mYAiNHvZNRjjTbN9ielX7Z2KcDKPEhTrtUG_QXPbzJIdwAPcfRUniI_cSPXtxsNsS1Oqz1tXPC-oMcOUmQMulbYNWhwlkCBW2-qSVvIDlo-T8BjX2qA0dd82CqO9CK5tcYNf1AyxcQih3qUE5q2oMHeZcegjIhv56AQ0pDfFvWdFTkU6hq5GiF-A9Y96S8EgM-bUdJ2g6nqNjQHxZEXtbZ8tQc0XTeSbVLDBc0-MaNnzDwY6m-1gHNNNM0_Y1v2ZJj9epwQs1JvMNauAxwzCgg4IdV5NNETDMg4DjgHLNCagFsdeuarB7J2tgOcKwAgE0xPI86K55AfAIT47fmIQKm_qBV3O8nUfHqt96MkLDNJlO0F9yGOzvxm0a-YvrItUOvPDL3q9oskmd6NI6zbOxG2LBzL1Jkq7oUcwgwu1wgRYSDbrE8ytsoJ1Bl5C4MoMuEWa1BB08e7UEHRxrZQZd7HqOEz0DkEvSSxm0X0BvAu1HUSyyzYkRMHzDnhtGWFbG92ZHSpiWcKVcK0YMN6bQ3WiOFKiU5ziopcFdmHIVrg55FA5jSVvy6ThR7MNNhaxo0Wfcj4Mwgu52LjurReeVp9BHTcZ8Cx0dn7bCWSrGYSZnRua7jmej6bJO9sIAvKRaXRh8XS0RJxM-zUD3QTTNRlL6X4IwlqZZdapbaWTADsSYK0sIxwX_8wqWkrWquGiwtHv91uwar9v1-Rv-k666YBKn0-TGykmccgpCTWPJialuGY6B58vMzosps335qpwwA2_LX0185bXzcykG2zacPnGa-alKqN5PyXtn_PFJPDIjtmMlz0nvKwSEycMH7sOnVR7oCGg5L4ZIXGiySZmgmcyT97fBcLcRswzThqirN8m4V726jfJ0KponMR-X9w_lRCQHhn_A0ySTBKdKk4tIeMm4SPaAoxGYB9QAbm__plPLti1CbCJ9uX35YMBbgoI2_ErIwsSbZ24IJRehRj6mjqVhwjChrQr4PlhCViQ44GDlr-H_x1OIkRxREyx9L0khiTfKkQ5CoL1HwI0hXKUROFVWVGakQ4SLc1AGZAQXc0F3mIaF46bKn3Dtb1D-iYiKruconBQt0Dw7KlXYvnfWqKx9e6epxkzAUc__06TMJlA7hXUgl6QwQHo-lxYDMWEMgS9Khpf9UWQp05FJThXKDc51fJJEQt5Bh2fPvkCNxolUxIOBzLURp2zvQ2C1TcFzjatjxhjWCNFN1bqt66J5y_cAWAA4OBTpq5msYcTDDFFK0cElBGLTAS4CvSk1zzITieYiUHtu-q-5yekEbFvrU7NvG6RP-4DYtq33tb7RN_t6a7K6Nn81d1lYfm9bWv1WT1p7McPMx2GkRj9Kc4cMhpBQzlH2Cs30_lZzn0VDzoozGSWxaBXZNnu4Pc3kzRGoRIlQmL6calZ2Dwlqg5dxqLZ2OWVb2IcqDXrzaqXAImtXCwOK-dXa7uF1YO3QNd4tx3oKN1AcZ1pE9l7LJZRoXuEOZcxvTFzdWMGguttgQ7HAGBU4VK3QagSwwYX676z-ll1e1X-t1EanS-21pVhpufM6VYY8o9HCqHtfloNep0m2laB3dcatwr-OeW-Of7amLcA_2xZpEGg7VqS_Ov5zqH0__AOaFEtqWxFZhVA3iJ68UHXYqBpXRZSsSwbUarKiDKi1MKqvea0IVbFyuc5gej4IJ9mMCNAzinM8jd_HyUWMp-cYEqA4wcV1AJ11zKupbynNUYLUIyQfofrRFfEmBXeah_GfSk71j-Y6xPc1F2s-l0tsLAY9OtfBXGdMEMuQfTEo7lQIxNXo2S_Vo\n"

    private val file = "{\n" +
      "  \"resourceType\" : \"AllergyIntolerance\",\n" +
      "  \"id\" : \"allergyintolerance-with-abatement\",\n" +
      "  \"text\" : {\n" +
      "    \"status\" : \"extensions\",\n" +
      "    \"div\" : \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><p><b>Generated Narrative: AllergyIntolerance</b><a name=\\\"allergyintolerance-with-abatement\\\"> </a></p><div style=\\\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\\\"><p style=\\\"margin-bottom: 0px\\\">Resource AllergyIntolerance &quot;allergyintolerance-with-abatement&quot; </p></div><p><b>Allergy abatement date</b>: 2010</p><p><b>clinicalStatus</b>: Resolved <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (<a href=\\\"http://terminology.hl7.org/5.0.0/CodeSystem-allergyintolerance-clinical.html\\\">AllergyIntolerance Clinical Status Codes</a>#resolved)</span></p><p><b>verificationStatus</b>: Confirmed <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (<a href=\\\"http://terminology.hl7.org/5.0.0/CodeSystem-allergyintolerance-verification.html\\\">AllergyIntolerance Verification Status</a>#confirmed)</span></p><p><b>code</b>: Ragweed pollen <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (<a href=\\\"https://browser.ihtsdotools.org/\\\">SNOMED CT</a>#256303006)</span></p><p><b>patient</b>: <a href=\\\"Patient-66033.html\\\">Patient/66033</a> &quot; LUX-BRENNARD&quot;</p><p><b>onset</b>: </p></div>\"\n" +
      "  },\n" +
      "  \"extension\" : [{\n" +
      "    \"url\" : \"http://hl7.org/fhir/uv/ips/StructureDefinition/abatement-dateTime-uv-ips\",\n" +
      "    \"valueDateTime\" : \"2010\"\n" +
      "  }],\n" +
      "  \"clinicalStatus\" : {\n" +
      "    \"coding\" : [{\n" +
      "      \"system\" : \"http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical\",\n" +
      "      \"code\" : \"resolved\"\n" +
      "    }]\n" +
      "  },\n" +
      "  \"verificationStatus\" : {\n" +
      "    \"coding\" : [{\n" +
      "      \"system\" : \"http://terminology.hl7.org/CodeSystem/allergyintolerance-verification\",\n" +
      "      \"code\" : \"confirmed\"\n" +
      "    }]\n" +
      "  },\n" +
      "  \"code\" : {\n" +
      "    \"coding\" : [{\n" +
      "      \"system\" : \"http://snomed.info/sct\",\n" +
      "      \"code\" : \"256303006\",\n" +
      "      \"display\" : \"Ragweed pollen\"\n" +
      "    }]\n" +
      "  },\n" +
      "  \"patient\" : {\n" +
      "    \"reference\" : \"Patient/66033\"\n" +
      "  },\n" +
      "  \"_onsetDateTime\" : {\n" +
      "    \"extension\" : [{\n" +
      "      \"url\" : \"http://hl7.org/fhir/StructureDefinition/data-absent-reason\",\n" +
      "      \"valueCode\" : \"unknown\"\n" +
      "    }]\n" +
      "  }\n" +
      "}"

    val file2 = "{\n" +
      "  \"resourceType\" : \"Bundle\",\n" +
      "  \"id\" : \"bundle-minimal\",\n" +
      "  \"language\" : \"en-US\",\n" +
      "  \"identifier\" : {\n" +
      "    \"system\" : \"urn:oid:2.16.724.4.8.10.200.10\",\n" +
      "    \"value\" : \"28b95815-76ce-457b-b7ae-a972e527db40\"\n" +
      "  },\n" +
      "  \"type\" : \"document\",\n" +
      "  \"timestamp\" : \"2020-12-11T14:30:00+01:00\",\n" +
      "  \"entry\" : [{\n" +
      "    \"fullUrl\" : \"urn:uuid:6e1fb74a-742b-4c7b-8487-171dacb88766\",\n" +
      "    \"resource\" : {\n" +
      "      \"resourceType\" : \"Composition\",\n" +
      "      \"id\" : \"6e1fb74a-742b-4c7b-8487-171dacb88766\",\n" +
      "      \"text\" : {\n" +
      "        \"status\" : \"generated\",\n" +
      "        \"div\" : \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><p><b>Generated Narrative</b></p><div style=\\\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\\\"><p style=\\\"margin-bottom: 0px\\\">Resource \\\"6e1fb74a-742b-4c7b-8487-171dacb88766\\\" </p></div><p><b>status</b>: final</p><p><b>type</b>: Patient summary Document <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (<a href=\\\"https://loinc.org/\\\">LOINC</a>#60591-5)</span></p><p><b>date</b>: 2020-12-11 02:30:00+0100</p><p><b>author</b>: Beetje van Hulp, MD </p><p><b>title</b>: Patient Summary as of December 11, 2020 14:30</p><p><b>confidentiality</b>: N</p><blockquote><p><b>attester</b></p><p><b>mode</b>: legal</p><p><b>time</b>: 2020-12-11 02:30:00+0100</p><p><b>party</b>: Beetje van Hulp, MD </p></blockquote><blockquote><p><b>attester</b></p><p><b>mode</b>: legal</p><p><b>time</b>: 2020-12-11 02:30:00+0100</p><p><b>party</b>: Anorg Aniza Tion BV </p></blockquote><p><b>custodian</b>: Anorg Aniza Tion BV</p><h3>RelatesTos</h3><table class=\\\"grid\\\"><tr><td>-</td><td><b>Code</b></td><td><b>Target[x]</b></td></tr><tr><td>*</td><td>appends</td><td>id: 20e12ce3-857f-49c0-b888-cb670597f191</td></tr></table><h3>Events</h3><table class=\\\"grid\\\"><tr><td>-</td><td><b>Code</b></td><td><b>Period</b></td></tr><tr><td>*</td><td>care provision <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (<a href=\\\"http://terminology.hl7.org/3.1.0/CodeSystem-v3-ActClass.html\\\">ActClass</a>#PCPR)</span></td><td>?? --&gt; 2020-12-11 02:30:00+0100</td></tr></table></div>\"\n" +
      "      },\n" +
      "      \"status\" : \"final\",\n" +
      "      \"type\" : {\n" +
      "        \"coding\" : [{\n" +
      "          \"system\" : \"http://loinc.org\",\n" +
      "          \"code\" : \"60591-5\",\n" +
      "          \"display\" : \"Patient summary Document\"\n" +
      "        }]\n" +
      "      },\n" +
      "      \"subject\" : {\n" +
      "        \"reference\" : \"Patient/7685713c-e29e-4a75-8a90-45be7ba3be94\"\n" +
      "      },\n" +
      "      \"date\" : \"2020-12-11T14:30:00+01:00\",\n" +
      "      \"author\" : [{\n" +
      "        \"reference\" : \"Practitioner/98315ba9-ffea-41ef-b59b-a836c039858f\"\n" +
      "      }],\n" +
      "      \"title\" : \"Patient Summary as of December 11, 2020 14:30\",\n" +
      "      \"confidentiality\" : \"N\",\n" +
      "      \"attester\" : [{\n" +
      "        \"mode\" : \"legal\",\n" +
      "        \"time\" : \"2020-12-11T14:30:00+01:00\",\n" +
      "        \"party\" : {\n" +
      "          \"reference\" : \"Practitioner/98315ba9-ffea-41ef-b59b-a836c039858f\"\n" +
      "        }\n" +
      "      },\n" +
      "      {\n" +
      "        \"mode\" : \"legal\",\n" +
      "        \"time\" : \"2020-12-11T14:30:00+01:00\",\n" +
      "        \"party\" : {\n" +
      "          \"reference\" : \"Organization/bb6bdf4f-7fcb-4d44-96a5-b858ad031d1d\"\n" +
      "        }\n" +
      "      }],\n" +
      "      \"custodian\" : {\n" +
      "        \"reference\" : \"Organization/bb6bdf4f-7fcb-4d44-96a5-b858ad031d1d\"\n" +
      "      },\n" +
      "      \"relatesTo\" : [{\n" +
      "        \"code\" : \"appends\",\n" +
      "        \"targetIdentifier\" : {\n" +
      "          \"system\" : \"urn:oid:2.16.724.4.8.10.200.10\",\n" +
      "          \"value\" : \"20e12ce3-857f-49c0-b888-cb670597f191\"\n" +
      "        }\n" +
      "      }],\n" +
      "      \"event\" : [{\n" +
      "        \"code\" : [{\n" +
      "          \"coding\" : [{\n" +
      "            \"system\" : \"http://terminology.hl7.org/CodeSystem/v3-ActClass\",\n" +
      "            \"code\" : \"PCPR\"\n" +
      "          }]\n" +
      "        }],\n" +
      "        \"period\" : {\n" +
      "          \"end\" : \"2020-12-11T14:30:00+01:00\"\n" +
      "        }\n" +
      "      }],\n" +
      "      \"section\" : [{\n" +
      "        \"title\" : \"Active Problems\",\n" +
      "        \"code\" : {\n" +
      "          \"coding\" : [{\n" +
      "            \"system\" : \"http://loinc.org\",\n" +
      "            \"code\" : \"11450-4\",\n" +
      "            \"display\" : \"Problem list Reported\"\n" +
      "          }]\n" +
      "        },\n" +
      "        \"text\" : {\n" +
      "          \"status\" : \"generated\",\n" +
      "          \"div\" : \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><ul><li><div><b>Condition Name</b>: Menopausal Flushing</div><div><b>Code</b>: <span>198436008</span></div><div><b>Status</b>: <span>Active</span></div></li></ul></div>\"\n" +
      "        },\n" +
      "        \"entry\" : [{\n" +
      "          \"reference\" : \"Condition/ad84b7a2-b4dd-474e-bef3-0779e6cb595f\"\n" +
      "        }]\n" +
      "      },\n" +
      "      {\n" +
      "        \"title\" : \"Medication\",\n" +
      "        \"code\" : {\n" +
      "          \"coding\" : [{\n" +
      "            \"system\" : \"http://loinc.org\",\n" +
      "            \"code\" : \"10160-0\",\n" +
      "            \"display\" : \"History of Medication use Narrative\"\n" +
      "          }]\n" +
      "        },\n" +
      "        \"text\" : {\n" +
      "          \"status\" : \"generated\",\n" +
      "          \"div\" : \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><ul><li><div><b>Medication Name</b>: Oral anastrozole 1mg tablet</div><div><b>Code</b>: <span></span></div><div><b>Status</b>: <span>Active, started March 2015</span></div><div>Instructions: Take 1 time per day</div></li></ul></div>\"\n" +
      "        },\n" +
      "        \"entry\" : [{\n" +
      "          \"reference\" : \"MedicationStatement/6e883e5e-7648-485a-86de-3640a61601fe\"\n" +
      "        }]\n" +
      "      },\n" +
      "      {\n" +
      "        \"title\" : \"Allergies and Intolerances\",\n" +
      "        \"code\" : {\n" +
      "          \"coding\" : [{\n" +
      "            \"system\" : \"http://loinc.org\",\n" +
      "            \"code\" : \"48765-2\",\n" +
      "            \"display\" : \"Allergies and adverse reactions Document\"\n" +
      "          }]\n" +
      "        },\n" +
      "        \"text\" : {\n" +
      "          \"status\" : \"generated\",\n" +
      "          \"div\" : \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><ul><li><div><b>Allergy Name</b>: Pencillins</div><div><b>Verification Status</b>: Confirmed</div><div><b>Reaction</b>: <span>no information</span></div></li></ul></div>\"\n" +
      "        },\n" +
      "        \"entry\" : [{\n" +
      "          \"reference\" : \"AllergyIntolerance/fe2769fd-22c9-4307-9122-ee0466e5aebb\"\n" +
      "        }]\n" +
      "      }]\n" +
      "    }\n" +
      "  },\n" +
      "  {\n" +
      "    \"fullUrl\" : \"urn:uuid:7685713c-e29e-4a75-8a90-45be7ba3be94\",\n" +
      "    \"resource\" : {\n" +
      "      \"resourceType\" : \"Patient\",\n" +
      "      \"id\" : \"7685713c-e29e-4a75-8a90-45be7ba3be94\",\n" +
      "      \"text\" : {\n" +
      "        \"status\" : \"generated\",\n" +
      "        \"div\" : \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><p><b>Generated Narrative: Patient</b><a name=\\\"7685713c-e29e-4a75-8a90-45be7ba3be94\\\"> </a></p><div style=\\\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\\\"><p style=\\\"margin-bottom: 0px\\\">Resource Patient &quot;7685713c-e29e-4a75-8a90-45be7ba3be94&quot; </p></div><p><b>identifier</b>: id: 574687583</p><p><b>active</b>: true</p><p><b>name</b>: Martha DeLarosa </p><p><b>telecom</b>: <a href=\\\"tel:+31788700800\\\">+31788700800</a></p><p><b>gender</b>: female</p><p><b>birthDate</b>: 1972-05-01</p><p><b>address</b>: Laan Van Europa 1600 Dordrecht 3317 DB NL </p><h3>Contacts</h3><table class=\\\"grid\\\"><tr><td>-</td><td><b>Relationship</b></td><td><b>Name</b></td><td><b>Telecom</b></td><td><b>Address</b></td></tr><tr><td>*</td><td>mother <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (<a href=\\\"http://terminology.hl7.org/5.0.0/CodeSystem-v3-RoleCode.html\\\">RoleCode</a>#MTH)</span></td><td>Martha Mum </td><td><a href=\\\"tel:+33-555-20036\\\">+33-555-20036</a></td><td>Promenade des Anglais 111 Lyon 69001 FR </td></tr></table></div>\"\n" +
      "      },\n" +
      "      \"identifier\" : [{\n" +
      "        \"system\" : \"urn:oid:2.16.840.1.113883.2.4.6.3\",\n" +
      "        \"value\" : \"574687583\"\n" +
      "      }],\n" +
      "      \"active\" : true,\n" +
      "      \"name\" : [{\n" +
      "        \"family\" : \"DeLarosa\",\n" +
      "        \"given\" : [\"Martha\"]\n" +
      "      }],\n" +
      "      \"telecom\" : [{\n" +
      "        \"system\" : \"phone\",\n" +
      "        \"value\" : \"+31788700800\",\n" +
      "        \"use\" : \"home\"\n" +
      "      }],\n" +
      "      \"gender\" : \"female\",\n" +
      "      \"birthDate\" : \"1972-05-01\",\n" +
      "      \"address\" : [{\n" +
      "        \"line\" : [\"Laan Van Europa 1600\"],\n" +
      "        \"city\" : \"Dordrecht\",\n" +
      "        \"postalCode\" : \"3317 DB\",\n" +
      "        \"country\" : \"NL\"\n" +
      "      }],\n" +
      "      \"contact\" : [{\n" +
      "        \"relationship\" : [{\n" +
      "          \"coding\" : [{\n" +
      "            \"system\" : \"http://terminology.hl7.org/CodeSystem/v3-RoleCode\",\n" +
      "            \"code\" : \"MTH\"\n" +
      "          }]\n" +
      "        }],\n" +
      "        \"name\" : {\n" +
      "          \"family\" : \"Mum\",\n" +
      "          \"given\" : [\"Martha\"]\n" +
      "        },\n" +
      "        \"telecom\" : [{\n" +
      "          \"system\" : \"phone\",\n" +
      "          \"value\" : \"+33-555-20036\",\n" +
      "          \"use\" : \"home\"\n" +
      "        }],\n" +
      "        \"address\" : {\n" +
      "          \"line\" : [\"Promenade des Anglais 111\"],\n" +
      "          \"city\" : \"Lyon\",\n" +
      "          \"postalCode\" : \"69001\",\n" +
      "          \"country\" : \"FR\"\n" +
      "        }\n" +
      "      }]\n" +
      "    }\n" +
      "  },\n" +
      "  {\n" +
      "    \"fullUrl\" : \"urn:uuid:98315ba9-ffea-41ef-b59b-a836c039858f\",\n" +
      "    \"resource\" : {\n" +
      "      \"resourceType\" : \"Practitioner\",\n" +
      "      \"id\" : \"98315ba9-ffea-41ef-b59b-a836c039858f\",\n" +
      "      \"text\" : {\n" +
      "        \"status\" : \"generated\",\n" +
      "        \"div\" : \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><p><b>Generated Narrative: Practitioner</b><a name=\\\"98315ba9-ffea-41ef-b59b-a836c039858f\\\"> </a></p><div style=\\\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\\\"><p style=\\\"margin-bottom: 0px\\\">Resource Practitioner &quot;98315ba9-ffea-41ef-b59b-a836c039858f&quot; </p></div><p><b>identifier</b>: id: 129854633</p><p><b>active</b>: true</p><p><b>name</b>: Beetje van Hulp </p><h3>Qualifications</h3><table class=\\\"grid\\\"><tr><td>-</td><td><b>Code</b></td></tr><tr><td>*</td><td>Doctor of Medicine <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (degreeLicenseCertificate[2.7]#MD)</span></td></tr></table></div>\"\n" +
      "      },\n" +
      "      \"identifier\" : [{\n" +
      "        \"system\" : \"urn:oid:2.16.528.1.1007.3.1\",\n" +
      "        \"value\" : \"129854633\",\n" +
      "        \"assigner\" : {\n" +
      "          \"display\" : \"CIBG\"\n" +
      "        }\n" +
      "      }],\n" +
      "      \"active\" : true,\n" +
      "      \"name\" : [{\n" +
      "        \"family\" : \"van Hulp\",\n" +
      "        \"given\" : [\"Beetje\"]\n" +
      "      }],\n" +
      "      \"qualification\" : [{\n" +
      "        \"code\" : {\n" +
      "          \"coding\" : [{\n" +
      "            \"system\" : \"http://terminology.hl7.org/CodeSystem/v2-0360\",\n" +
      "            \"version\" : \"2.7\",\n" +
      "            \"code\" : \"MD\",\n" +
      "            \"display\" : \"Doctor of Medicine\"\n" +
      "          }]\n" +
      "        }\n" +
      "      }]\n" +
      "    }\n" +
      "  },\n" +
      "  {\n" +
      "    \"fullUrl\" : \"urn:uuid:bb6bdf4f-7fcb-4d44-96a5-b858ad031d1d\",\n" +
      "    \"resource\" : {\n" +
      "      \"resourceType\" : \"Organization\",\n" +
      "      \"id\" : \"bb6bdf4f-7fcb-4d44-96a5-b858ad031d1d\",\n" +
      "      \"text\" : {\n" +
      "        \"status\" : \"generated\",\n" +
      "        \"div\" : \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><p><b>Generated Narrative: Organization</b><a name=\\\"bb6bdf4f-7fcb-4d44-96a5-b858ad031d1d\\\"> </a></p><div style=\\\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\\\"><p style=\\\"margin-bottom: 0px\\\">Resource Organization &quot;bb6bdf4f-7fcb-4d44-96a5-b858ad031d1d&quot; </p></div><p><b>identifier</b>: id: 564738757</p><p><b>active</b>: true</p><p><b>name</b>: Anorg Aniza Tion BV / The best custodian ever</p><p><b>telecom</b>: <a href=\\\"tel:+31-51-34343400\\\">+31-51-34343400</a></p><p><b>address</b>: Houttuinen 27 Dordrecht 3311 CE NL (WORK)</p></div>\"\n" +
      "      },\n" +
      "      \"identifier\" : [{\n" +
      "        \"system\" : \"urn:oid:2.16.528.1.1007.3.3\",\n" +
      "        \"value\" : \"564738757\"\n" +
      "      }],\n" +
      "      \"active\" : true,\n" +
      "      \"name\" : \"Anorg Aniza Tion BV / The best custodian ever\",\n" +
      "      \"telecom\" : [{\n" +
      "        \"system\" : \"phone\",\n" +
      "        \"value\" : \"+31-51-34343400\",\n" +
      "        \"use\" : \"work\"\n" +
      "      }],\n" +
      "      \"address\" : [{\n" +
      "        \"use\" : \"work\",\n" +
      "        \"line\" : [\"Houttuinen 27\"],\n" +
      "        \"city\" : \"Dordrecht\",\n" +
      "        \"postalCode\" : \"3311 CE\",\n" +
      "        \"country\" : \"NL\"\n" +
      "      }]\n" +
      "    }\n" +
      "  },\n" +
      "  {\n" +
      "    \"fullUrl\" : \"urn:uuid:ad84b7a2-b4dd-474e-bef3-0779e6cb595f\",\n" +
      "    \"resource\" : {\n" +
      "      \"resourceType\" : \"Condition\",\n" +
      "      \"id\" : \"ad84b7a2-b4dd-474e-bef3-0779e6cb595f\",\n" +
      "      \"text\" : {\n" +
      "        \"status\" : \"generated\",\n" +
      "        \"div\" : \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><p><b>Generated Narrative: Condition</b><a name=\\\"ad84b7a2-b4dd-474e-bef3-0779e6cb595f\\\"> </a></p><div style=\\\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\\\"><p style=\\\"margin-bottom: 0px\\\">Resource Condition &quot;ad84b7a2-b4dd-474e-bef3-0779e6cb595f&quot; </p></div><p><b>identifier</b>: id: cacceb57-395f-48e1-9c88-e9c9704dc2d2</p><p><b>clinicalStatus</b>: Active <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (<a href=\\\"http://terminology.hl7.org/5.0.0/CodeSystem-condition-clinical.html\\\">Condition Clinical Status Codes</a>#active)</span></p><p><b>verificationStatus</b>: Confirmed <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (<a href=\\\"http://terminology.hl7.org/5.0.0/CodeSystem-condition-ver-status.html\\\">ConditionVerificationStatus</a>#confirmed)</span></p><p><b>category</b>: Problem <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (<a href=\\\"https://loinc.org/\\\">LOINC</a>#75326-9)</span></p><p><b>severity</b>: Moderate <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (<a href=\\\"https://loinc.org/\\\">LOINC</a>#LA6751-7)</span></p><p><b>code</b>: Menopausal flushing (finding) <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (<a href=\\\"https://browser.ihtsdotools.org/\\\">SNOMED CT</a>#198436008; <a href=\\\"http://terminology.hl7.org/5.0.0/CodeSystem-icd10.html\\\">ICD-10</a>#N95.1 &quot;Menopausal and female climacteric states&quot;)</span></p><p><b>subject</b>: <a href=\\\"#Patient_7685713c-e29e-4a75-8a90-45be7ba3be94\\\">See above (Patient/7685713c-e29e-4a75-8a90-45be7ba3be94)</a></p><p><b>onset</b>: 2015</p><p><b>recordedDate</b>: 2016-10</p></div>\"\n" +
      "      },\n" +
      "      \"identifier\" : [{\n" +
      "        \"system\" : \"urn:oid:1.2.3.999\",\n" +
      "        \"value\" : \"cacceb57-395f-48e1-9c88-e9c9704dc2d2\"\n" +
      "      }],\n" +
      "      \"clinicalStatus\" : {\n" +
      "        \"coding\" : [{\n" +
      "          \"system\" : \"http://terminology.hl7.org/CodeSystem/condition-clinical\",\n" +
      "          \"code\" : \"active\"\n" +
      "        }]\n" +
      "      },\n" +
      "      \"verificationStatus\" : {\n" +
      "        \"coding\" : [{\n" +
      "          \"system\" : \"http://terminology.hl7.org/CodeSystem/condition-ver-status\",\n" +
      "          \"code\" : \"confirmed\"\n" +
      "        }]\n" +
      "      },\n" +
      "      \"category\" : [{\n" +
      "        \"coding\" : [{\n" +
      "          \"system\" : \"http://loinc.org\",\n" +
      "          \"code\" : \"75326-9\",\n" +
      "          \"display\" : \"Problem\"\n" +
      "        }]\n" +
      "      }],\n" +
      "      \"severity\" : {\n" +
      "        \"coding\" : [{\n" +
      "          \"system\" : \"http://loinc.org\",\n" +
      "          \"code\" : \"LA6751-7\",\n" +
      "          \"display\" : \"Moderate\"\n" +
      "        }]\n" +
      "      },\n" +
      "      \"code\" : {\n" +
      "        \"coding\" : [{\n" +
      "          \"system\" : \"http://snomed.info/sct\",\n" +
      "          \"code\" : \"198436008\",\n" +
      "          \"display\" : \"Menopausal flushing (finding)\",\n" +
      "          \"_display\" : {\n" +
      "            \"extension\" : [{\n" +
      "              \"extension\" : [{\n" +
      "                \"url\" : \"lang\",\n" +
      "                \"valueCode\" : \"nl-NL\"\n" +
      "              },\n" +
      "              {\n" +
      "                \"url\" : \"content\",\n" +
      "                \"valueString\" : \"opvliegers\"\n" +
      "              }],\n" +
      "              \"url\" : \"http://hl7.org/fhir/StructureDefinition/translation\"\n" +
      "            }]\n" +
      "          }\n" +
      "        },\n" +
      "        {\n" +
      "          \"system\" : \"http://hl7.org/fhir/sid/icd-10\",\n" +
      "          \"code\" : \"N95.1\",\n" +
      "          \"display\" : \"Menopausal and female climacteric states\"\n" +
      "        }]\n" +
      "      },\n" +
      "      \"subject\" : {\n" +
      "        \"reference\" : \"Patient/7685713c-e29e-4a75-8a90-45be7ba3be94\"\n" +
      "      },\n" +
      "      \"onsetDateTime\" : \"2015\",\n" +
      "      \"recordedDate\" : \"2016-10\"\n" +
      "    }\n" +
      "  },\n" +
      "  {\n" +
      "    \"fullUrl\" : \"urn:uuid:6e883e5e-7648-485a-86de-3640a61601fe\",\n" +
      "    \"resource\" : {\n" +
      "      \"resourceType\" : \"MedicationStatement\",\n" +
      "      \"id\" : \"6e883e5e-7648-485a-86de-3640a61601fe\",\n" +
      "      \"text\" : {\n" +
      "        \"status\" : \"generated\",\n" +
      "        \"div\" : \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><p><b>Generated Narrative: MedicationStatement</b><a name=\\\"6e883e5e-7648-485a-86de-3640a61601fe\\\"> </a></p><div style=\\\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\\\"><p style=\\\"margin-bottom: 0px\\\">Resource MedicationStatement &quot;6e883e5e-7648-485a-86de-3640a61601fe&quot; </p></div><p><b>identifier</b>: id: 8faf0319-89d3-427c-b9d1-e8c8fd390dca</p><p><b>status</b>: active</p><p><b>medication</b>: <a href=\\\"#Medication_6369a973-afc7-4617-8877-3e9811e05a5b\\\">See above (Medication/6369a973-afc7-4617-8877-3e9811e05a5b)</a></p><p><b>subject</b>: <a href=\\\"#Patient_7685713c-e29e-4a75-8a90-45be7ba3be94\\\">See above (Patient/7685713c-e29e-4a75-8a90-45be7ba3be94)</a></p><p><b>effective</b>: 2015-03 --&gt; (ongoing)</p><blockquote><p><b>dosage</b></p><p><b>timing</b>: Count 1 times, Do Once</p><p><b>route</b>: Oral use <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (standardterms.edqm.eu#20053000)</span></p></blockquote></div>\"\n" +
      "      },\n" +
      "      \"identifier\" : [{\n" +
      "        \"system\" : \"urn:oid:1.2.3.999\",\n" +
      "        \"value\" : \"8faf0319-89d3-427c-b9d1-e8c8fd390dca\"\n" +
      "      }],\n" +
      "      \"status\" : \"active\",\n" +
      "      \"medicationReference\" : {\n" +
      "        \"reference\" : \"Medication/6369a973-afc7-4617-8877-3e9811e05a5b\"\n" +
      "      },\n" +
      "      \"subject\" : {\n" +
      "        \"reference\" : \"Patient/7685713c-e29e-4a75-8a90-45be7ba3be94\"\n" +
      "      },\n" +
      "      \"effectivePeriod\" : {\n" +
      "        \"start\" : \"2015-03\"\n" +
      "      },\n" +
      "      \"dosage\" : [{\n" +
      "        \"timing\" : {\n" +
      "          \"repeat\" : {\n" +
      "            \"count\" : 1,\n" +
      "            \"periodUnit\" : \"d\"\n" +
      "          }\n" +
      "        },\n" +
      "        \"route\" : {\n" +
      "          \"coding\" : [{\n" +
      "            \"system\" : \"http://standardterms.edqm.eu\",\n" +
      "            \"code\" : \"20053000\",\n" +
      "            \"display\" : \"Oral use\"\n" +
      "          }]\n" +
      "        },\n" +
      "        \"doseAndRate\" : [{\n" +
      "          \"type\" : {\n" +
      "            \"coding\" : [{\n" +
      "              \"system\" : \"http://terminology.hl7.org/CodeSystem/dose-rate-type\",\n" +
      "              \"code\" : \"ordered\",\n" +
      "              \"display\" : \"Ordered\"\n" +
      "            }]\n" +
      "          },\n" +
      "          \"doseQuantity\" : {\n" +
      "            \"value\" : 1,\n" +
      "            \"unit\" : \"tablet\",\n" +
      "            \"system\" : \"http://unitsofmeasure.org\",\n" +
      "            \"code\" : \"1\"\n" +
      "          }\n" +
      "        }]\n" +
      "      }]\n" +
      "    }\n" +
      "  },\n" +
      "  {\n" +
      "    \"fullUrl\" : \"urn:uuid:6369a973-afc7-4617-8877-3e9811e05a5b\",\n" +
      "    \"resource\" : {\n" +
      "      \"resourceType\" : \"Medication\",\n" +
      "      \"id\" : \"6369a973-afc7-4617-8877-3e9811e05a5b\",\n" +
      "      \"text\" : {\n" +
      "        \"status\" : \"generated\",\n" +
      "        \"div\" : \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><p><b>Generated Narrative: Medication</b><a name=\\\"6369a973-afc7-4617-8877-3e9811e05a5b\\\"> </a></p><div style=\\\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\\\"><p style=\\\"margin-bottom: 0px\\\">Resource Medication &quot;6369a973-afc7-4617-8877-3e9811e05a5b&quot; </p></div><p><b>code</b>: Product containing anastrozole (medicinal product) <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (<a href=\\\"https://browser.ihtsdotools.org/\\\">SNOMED CT</a>#108774000; unknown#99872 &quot;ANASTROZOL 1MG TABLET&quot;; unknown#2076667 &quot;ANASTROZOL CF TABLET FILMOMHULD 1MG&quot;; <a href=\\\"http://terminology.hl7.org/5.0.0/CodeSystem-v3-WC.html\\\">WHO ATC</a>#L02BG03 &quot;anastrozole&quot;)</span></p></div>\"\n" +
      "      },\n" +
      "      \"code\" : {\n" +
      "        \"coding\" : [{\n" +
      "          \"system\" : \"http://snomed.info/sct\",\n" +
      "          \"code\" : \"108774000\",\n" +
      "          \"display\" : \"Product containing anastrozole (medicinal product)\"\n" +
      "        },\n" +
      "        {\n" +
      "          \"system\" : \"urn:oid:2.16.840.1.113883.2.4.4.1\",\n" +
      "          \"code\" : \"99872\",\n" +
      "          \"display\" : \"ANASTROZOL 1MG TABLET\"\n" +
      "        },\n" +
      "        {\n" +
      "          \"system\" : \"urn:oid:2.16.840.1.113883.2.4.4.7\",\n" +
      "          \"code\" : \"2076667\",\n" +
      "          \"display\" : \"ANASTROZOL CF TABLET FILMOMHULD 1MG\"\n" +
      "        },\n" +
      "        {\n" +
      "          \"system\" : \"http://www.whocc.no/atc\",\n" +
      "          \"code\" : \"L02BG03\",\n" +
      "          \"display\" : \"anastrozole\"\n" +
      "        }]\n" +
      "      }\n" +
      "    }\n" +
      "  },\n" +
      "  {\n" +
      "    \"fullUrl\" : \"urn:uuid:fe2769fd-22c9-4307-9122-ee0466e5aebb\",\n" +
      "    \"resource\" : {\n" +
      "      \"resourceType\" : \"AllergyIntolerance\",\n" +
      "      \"id\" : \"fe2769fd-22c9-4307-9122-ee0466e5aebb\",\n" +
      "      \"text\" : {\n" +
      "        \"status\" : \"generated\",\n" +
      "        \"div\" : \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><p><b>Generated Narrative: AllergyIntolerance</b><a name=\\\"fe2769fd-22c9-4307-9122-ee0466e5aebb\\\"> </a></p><div style=\\\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\\\"><p style=\\\"margin-bottom: 0px\\\">Resource AllergyIntolerance &quot;fe2769fd-22c9-4307-9122-ee0466e5aebb&quot; </p></div><p><b>identifier</b>: id: 8d9566a4-d26d-46be-a3e4-c9f3a0e5cd83</p><p><b>clinicalStatus</b>: Active <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (<a href=\\\"http://terminology.hl7.org/5.0.0/CodeSystem-allergyintolerance-clinical.html\\\">AllergyIntolerance Clinical Status Codes</a>#active)</span></p><p><b>verificationStatus</b>: Confirmed <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (<a href=\\\"http://terminology.hl7.org/5.0.0/CodeSystem-allergyintolerance-verification.html\\\">AllergyIntolerance Verification Status</a>#confirmed)</span></p><p><b>type</b>: allergy</p><p><b>category</b>: medication</p><p><b>criticality</b>: high</p><p><b>code</b>: Substance with penicillin structure and antibacterial mechanism of action (substance) <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (<a href=\\\"https://browser.ihtsdotools.org/\\\">SNOMED CT</a>#373270004)</span></p><p><b>patient</b>: <a href=\\\"#Patient_7685713c-e29e-4a75-8a90-45be7ba3be94\\\">See above (Patient/7685713c-e29e-4a75-8a90-45be7ba3be94)</a></p><p><b>onset</b>: 2010</p></div>\"\n" +
      "      },\n" +
      "      \"identifier\" : [{\n" +
      "        \"system\" : \"urn:oid:1.2.3.999\",\n" +
      "        \"value\" : \"8d9566a4-d26d-46be-a3e4-c9f3a0e5cd83\"\n" +
      "      }],\n" +
      "      \"clinicalStatus\" : {\n" +
      "        \"coding\" : [{\n" +
      "          \"system\" : \"http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical\",\n" +
      "          \"code\" : \"active\"\n" +
      "        }]\n" +
      "      },\n" +
      "      \"verificationStatus\" : {\n" +
      "        \"coding\" : [{\n" +
      "          \"system\" : \"http://terminology.hl7.org/CodeSystem/allergyintolerance-verification\",\n" +
      "          \"code\" : \"confirmed\"\n" +
      "        }]\n" +
      "      },\n" +
      "      \"type\" : \"allergy\",\n" +
      "      \"category\" : [\"medication\"],\n" +
      "      \"criticality\" : \"high\",\n" +
      "      \"code\" : {\n" +
      "        \"coding\" : [{\n" +
      "          \"system\" : \"http://snomed.info/sct\",\n" +
      "          \"code\" : \"373270004\",\n" +
      "          \"display\" : \"Substance with penicillin structure and antibacterial mechanism of action (substance)\"\n" +
      "        }]\n" +
      "      },\n" +
      "      \"patient\" : {\n" +
      "        \"reference\" : \"Patient/7685713c-e29e-4a75-8a90-45be7ba3be94\"\n" +
      "      },\n" +
      "      \"onsetDateTime\" : \"2010\"\n" +
      "    }\n" +
      "  }]\n" +
      "}"
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
        val extractedVCWithFunc = urlUtilsMock.extractVerifiableCredential(verifiableCredential)
        Assert.assertEquals(extractedVCWithFunc, extractedCredential)
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
        val encryptionKey = urlUtilsMock.generateRandomKey()
        // Log.d("enc key", encryptionKey)
        println(encryptionKey)

        // need to encode and compress the payload
        // val encodedPayload = urlUtils.encodeAndCompressPayload(file, encryptionKey)

        val contentJson = Gson().toJson(file)
        val contentEncrypted = urlUtilsMock.encryptContent(contentJson, encryptionKey)
        println(contentEncrypted)
    }

}