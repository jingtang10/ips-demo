package com.example.ipsapp

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
    fun jsonElementsCanBeSplitIntoIndividualFiles() {
        // val response = "{\"files\":[{\"contentType\":\"application/smart-health-card\",\"embedded\":\"eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIiwiY29udGVudFR5cGUiOiJhcHBsaWNhdGlvbi9zbWFydC1oZWFsdGgtY2FyZCJ9..2Jh7TJGW_0uZ1YzL.qC1qFN-zGOVx21LZtQcjXaxzKU1OHMS51giVk8RvGmaRaj-GRvnvhwvwRBxvZAZSBU42lBxj3cgXCnUgTv0DI3MBBIUCoRL7X-D3eFBZb_R3OhFlA1ckGYutLkN0UFvbOFMa72qKIJ0U7dEkrANcRgwgk7aoR__UfW4GRcEKCMcT-QBKi9SlD9-LVEhKgaUwwDBH8rsRxDLvWCN0FaBpUBMHeFxWpZTFEELKaQTQMspAYF7fPSHQllbP53ujWvBWZRUeqIvDNEzCnckjMD5hxhiUPgeOBdCjL5A6D0Zz-pOcQgDR5FnxTJIXqOsmaAbSiv4l-uXnlojmfrdypnJXyX9xM_nDmUtqZoNm4igKmNN7qTYZcfn6CNX3RXG1Y8SIL6HB9aqhMwLjpxXFf639GimRaheR85D2Ah_nW3jmBBiCkz5K8KGWKe7NM-KIpiQCCdjBodlYWciN-YZJKpl9NmABGI6TXWCkKuvo3uFjJp0x-ewp-9B4-l3FTagejwRbclUwDdFKE38tQRq5uOGD4fd0j4G3-tia9ioIrmVYx-Vs1flcSolFdwwn4AWa75wssMb145kWuHcRdrkKbWrhhkeyU9l2JA6xniVCy4yqfU1bJCgs1bRdCzaCmYij13avOTRQtc_lDr6fVw2Tl-gXb7ctw1qtlXXB3xBZNdyMB_q-XageqTCUpDpOqhqlNJJhRaJOjxs9snnTdrki_omnrur0bWnnqOBIohFXuccLoiIyPhefDst5zGknzQX5WxIsrQksC3AzDaXcQ_EAB9IkOAYqFWz-qcb7n9RBRS-xy2rTS2woz_2YgSnFuE6C-MCVrAIJm4GhpIgspCfN7FMeK_onsLpkKuRVw8i33EVLmiAY0ky_bzV-tkKWI1tFuaI61ohuatQ5GZ9bffpPO3iFcTJRbna1_LKWl_Fa7SG-dzvxowFwQBIZve9B0slsXP0el_mN_CCrHqTXqR24jVQobH3x2r-4.WVTlwLM45EmpXCrGa2jwcg\",\"location\":\"https://api.vaxx.link/api/shl/KTseQR32kkQ4Q04aIjM1niOuxDOn1jZ_vYQb5jfIqCs/file/5u1vBqIU4b5FVuFySSShhUlwm2miwHpAcQNoxVrdZd4?ticket=jxTl7iwLe8_f3gmlk2MnlUIc5ZaPwtGpOeUb--nlewM\"},{\"contentType\":\"application/smart-health-card\",\"embedded\":\"eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIiwiY29udGVudFR5cGUiOiJhcHBsaWNhdGlvbi9zbWFydC1oZWFsdGgtY2FyZCJ9..ZfOGeSFpTKzAQBif.BnC8H9f5gtpytJ23L0GusWNDNtGiaUCZboYCi2BP2VKxkR14ijlfSKu-pUu_oRBhYU8yt9IACmmLoAb0DKVvyimhk-thZ_vVtodLf56R4NdrZ7OZng01QE4wF5wi6GD2Us-irdA6Fd1Aj5l9OpoE4lhA9czOjZXRuW64ohlukkHXBP2kyaLf1HCsUCbCKfOw0p45dX8sabUuiRnfdN0TKHA5umOH5aEy-WBfrDDTvY1H58kNXs43uQWQxIhJLjlnj2C2XyU0ZfBZaR5ilKUL9P4myuEYzGxynsDfBorYn5YDFLpyDLoteBszxoteRzI6rc8hJNL7wFm2ypCtqVdMpBtBDIDsWYa-_o1tNNAky-45_hrOIxOQbFURterIo1j-sn0IsJr6NJKssb35Vy_dGD-EpdzW2-symVCwM1bwVLQ4GKAOpL-FKdvD8Kd-B8uZjg7qDgC1S1GK4uXA1-h7G3S52tKftFftH0i2nCUtv6cVlK8DqS6-3sgIgElusfLK56wBIOMol_TeRE3JsARVswEBh8nB-oT3VJEVgAtW-ldwBpuFfi_zEEzF__NOTOp_rbF_tYNyTw3yYmSG9KXizkXXC8IAKe3QfWmm7wb81OyPoJZN3ZCz00ArYKKOAL3NZX7nkH6bBv1nAewWN0ItuFLY8UkUtSFVW3s9gq0gF_prqrZoaJ9S960ChC7PV4U1TBYlZuMY1ZMdV2zv1jJ5ZDa3Mea7wp6iOucg3WhJalsKarVOuWi8DW2rVA0zht8YFgHO1YiKXR8IQpPtqV-P3pe6iOjW5K0_1ePiwS1qkOQFNoCdIvWzSI-ysBpAQJNHSMH4WCrfnJ0wF2L4upZ-NScqqdLW_3T-g4LKlXvZzNxT6ITblXk_djRSN6YPy6xt_NXhKdMznqMemqd6UBnHx839XxmbVsZuQYUHnMpP5KJYXh3UmRPirV3ClZhR9JCMUoDhLWdvwAXgpWMy5YGv4pZCTIDw8zg3ohtMh-MXqChTQX4.DMMjGhKC6JHfiYeNrQ2Pjw\",\"location\":\"https://api.vaxx.link/api/shl/KTseQR32kkQ4Q04aIjM1niOuxDOn1jZ_vYQb5jfIqCs/file/kW0P5AWkr_1EkbI028kkNIf7FKyBaLsVH_3GiI-VDkw?ticket=jxTl7iwLe8_f3gmlk2MnlUIc5ZaPwtGpOeUb--nlewM\"},{\"contentType\":\"application/smart-health-card\",\"embedded\":\"eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIiwiY29udGVudFR5cGUiOiJhcHBsaWNhdGlvbi9zbWFydC1oZWFsdGgtY2FyZCJ9..WaZkYbs939I9VTB_.DjS43JZwaDHA1TMJNV-VaNwed7Jjn5jt1ckDnyTaH1RChg-yr6Qm-8oDAkDZuaZ2j8O0JQhfBKdSdI-o6wByn6jMp0pqiZEL3o821QcY5tABt_g-N3R0b1R0HYdTj3Yyjhuxx6DWTSYRqP5HN_--juH4fS7CVmaNFKh0E_0-vIB4aJbqXQKl081Pad-x1m2F4DX3A_c-VfIsvAptuvszHh9HXuqgJAv9KH8DF59oSBz-jd-qskoVNNraeay_eZNOFMJ7SH2ArupA-C3i1iYMWVKdPTSpY8L3uTPnewP5rAuMVfWagXS2yGl4HrxowdcgVhCTuQLjS2QMa7PhE-J3DQTCUg1h7WhszsJPRHQNjuP-BnpsKZUv0N6PtP9rY5Fdwde8nDus_DgkQZxFPd2XlFVu2usuMn5V0qtg3o7P53VpVAQhD9Z5VveeHd9HmHEkFciUa49W9cTsVdUeHTu3MgQpgdVfJAzadvYL0YyElGXmBEPZZsqxHr7Pb4BSF3WjnvJwWsUbsxdOcO5pcCMRnrKy_FralB344LYX2QU5ZoDRyZFzL5QE-YkiL_OFTxxJIihq4BPpfUFv8jGtroDJqkMoMQpvy0RM8sQY2Bk18v9Xkb49Df9JOROxLJLFxoKPuromvGqjDcW543w5b5J90ayH0mpTbfkOMdexqfUn1SRm6OtYR822329HkzTOCX-SWh8ptHj4yABpxahkfrFNs4Qm0vS4mTscZQGU9iopsTcSU8W0_lKJvrJwRz7ZSzxjKVy8k7ehI4zMIY7NW-Ol38BJf385TyrzgUnoEdbnPfzD7JSYtCwBtHFvXH7OluP8TNpVzYKqTUq_HhGd10dM_0pZCALy-iZJfRsf8UPDDJUr8f5eI3pCnnDuLhxx885ZaR0xw8JrMswT3dZPHu8P_T7LZG90nqrg9vHyPDB1naopnXuZvTBkPrJc7LD2MZzV9cmm4RkbZgQGjXFt7SMpvSN2MfWa8fntqosrF9Scr9I0Bg.KPtTkXHhAXmccjwbDnESZQ\",\"location\":\"https://api.vaxx.link/api/shl/KTseQR32kkQ4Q04aIjM1niOuxDOn1jZ_vYQb5jfIqCs/file/cFFvH346TJzrCb71NHJ6NO3FFvJt0arOik5th1e1nLU?ticket=jxTl7iwLe8_f3gmlk2MnlUIc5ZaPwtGpOeUb--nlewM\"},{\"contentType\":\"application/smart-health-card\",\"embedded\":\"eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIiwiY29udGVudFR5cGUiOiJhcHBsaWNhdGlvbi9zbWFydC1oZWFsdGgtY2FyZCJ9..8SOWvVQkDYMOAFRR.APO8_Wner7HXWl2q7_aSo8yBl-5cdYoxn4Mu2hmb8H81X2-lNUETe3Wz6H5GG-pE4ajyYYB0m_OZu-Rsvc89LSdiqpwzlM-X7GPttwlTXBFGV6qtAVCNaYaR1XrhEeQ7qvamx3qYWn6iou5weKxLTQ2pGLQzwjzEJmx6YUWUw1Y4KPnVsV3EiS2Sc2OuYfCFhOxWZKATmi4Ft_Uh5fzv7a-2jiBHIUk1TsnKWWlSW9JTQPR86uMY5yhsMgwZMFpykEL22XIz59nkerta99lWNPhkIx4sH-sq2dwDBTFsAYkFQVltP0cy5vEjjnl6whFxxu00CeSS0uG6lgnfKj9ZuXQT89YEkkNHq5-ePfFv8MmVkaKjnuDMLMMTberojXXHBHJHzG7JhzWE7oKBj2spA7TDcDopTnT9-bkj2n6dY3r0Sm0NBvvqfwlKVDw4vMq79egH9-J1s0ywqqD2M3kRXXRypPiIYb0uZkRpFnbc8WB8tLfXqL9FrHpA81rcuqTA2eV_mjiRGVrWYwhilXaXGVpCpFOCWC0R5OIsWkAZ_T7-xWUTcJrCKOTcBUCz0mddhmZJDhabpj0WSVoF9DutQLu_gMzFOOtzAq6n_u9-eK0IQ_feWOGNEXEfF0NIR-6zdlKDx_V4f8olbM40jJQay5aigmj6_tW1NdvfTHv-2F8YF99JQSzARxhmAwK0ve835Pt_SfiIZQotZp8QApt7Cav8xzFjwBWoELrq4P76jSV-MJzu10GvI9s8pMCymdvniV80ZVZxghQo6ndvrEDm8pAtWIATZ3y57FBiOWPrc1rnXkY3Nz2QkTiiMYoA0npt37o72LlkLwRm5W_dzYxEd5kiIstNpND-e4nvWubGu51ahymlFJC-N6Fh8AC_3EylPQA3ub4KW5K8T8jGktIell7hBWLSha44XAJ2LT1bY-xmLHTrcIvU8pU0wMMMoPOSmaYrC0No6UBlCmZwe626A4y6L9cpTH6yqMnZWm0M1sYffw.H_hYcOrcQRpgI08IguJBww\",\"location\":\"https://api.vaxx.link/api/shl/KTseQR32kkQ4Q04aIjM1niOuxDOn1jZ_vYQb5jfIqCs/file/t0jfKBClpH0HYPr879HdoHkBD9pW_Vl995ZS55nEEcY?ticket=jxTl7iwLe8_f3gmlk2MnlUIc5ZaPwtGpOeUb--nlewM\"},{\"contentType\":\"application/smart-health-card\",\"embedded\":\"eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIiwiY29udGVudFR5cGUiOiJhcHBsaWNhdGlvbi9zbWFydC1oZWFsdGgtY2FyZCJ9..Dp-2nqHpt2mkuLr8.UR2hTgg2Qoa05zwJQBforANJ42rU2rgFepDwNKAjjvAuLUEuU5bHlylOeOtD7KmHCsB_ehp_I3SaMQWteaGPqecrmq3HzNo_Gvl_niYwHUeS4-v-kV4F6tzZl1tlng1Eod2R8kl6Vf3guMGbo0OSnyT-U5CGWaWi-i29Alxggi5PGM8lVGR6Ce9FHcK4xsfY17PcnqT1cs9lm3JDklOQNfAUCkdjh8jN9Q_Pkr5HN87lwq_vtMershSRaWi0h4iP2e3wQCKVqiRHnEeSc-QTGv8ARN3M4qmYxoGe9lIlPlMA7wfNn4znimvueYlG4D88uYNQlwSvFQTO3upJ2dQ2YpJLyur7s1lFY0vZPjCWf52I3DDMO2c4QtRcNdHuctlhPWMqHUZqE9xRqrO-SJkTXqrVYYZ0X2cXwjlzGTEicnVEZO0clpQYdwkElfQjwC-8Mms1jrrmPV93Sc4akoxE046AHcrd0UUfzjg8EN3Mnujd-GixlhokAS7xGwdi_Fm19HTKYsiiYLjnulu7uJHvxkheccIi8N7Xj1oT5SWL5PxTcgXw2mKAn0uQCCnq311PcHnMQ5_7xUytVlKFloV4qFw0yfekZp0_D6V6t0MffF7VDvrssQj3bULIfdxbayoZbOrY-V6m-HIv_aAYbFnzQW2n5g8-OAAo4MKygz5EnunI_KR3fXkaQ0lfrEvwc_-9aScJiIUe2-tLSgyYga7QNCB0dAqJsC_bt_zSs5e5n81aLkOGLze4U2Q7QR1wqPOCEK-rm-9hZaMpP-WUbAluqswPD-ybDJW4heL9wTa5JKfV1pKa4S4QGj05e33AFKU2vLxV5E3gkFr20WHItQhdGGKOJ0KigxC3P22puYhl88rLw5c-Df3VN7ULPlj1gtehmZjPq1ZiE8dF_-VsF03GbkdRp2NdLRnuK-sT0Oss5csVjmWbQOR-zCPv6Ewz0FL-9QoVR7q3xrX-cVE0_nb63ecIp9Fkwl0c4lJNtiTw38QeIhk.IlMX0IeUwOV5HUUSkY9V-g\",\"location\":\"https://api.vaxx.link/api/shl/KTseQR32kkQ4Q04aIjM1niOuxDOn1jZ_vYQb5jfIqCs/file/2xqDepopSvoVqJUeUlJDw1XfhYODTvE4EXpMZaDH7yM?ticket=jxTl7iwLe8_f3gmlk2MnlUIc5ZaPwtGpOeUb--nlewM\"},{\"contentType\":\"application/smart-health-card\",\"embedded\":\"eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIiwiY29udGVudFR5cGUiOiJhcHBsaWNhdGlvbi9zbWFydC1oZWFsdGgtY2FyZCJ9..eP9VrvTSHH1TUtXg.ECGQLiojP5dHe13l4ncOBMeqUzPQuNGOhrnkDw8rlJLCeIwrN-Zqg7_Myf8UNQqb-amxNxEaA5Ne7OynbLEWYsuYeRa3L1m4c1T59kA4DqcMg1B_utS7_kz8fNK-nlt--1oDu79yCxOGnMx4Y61CaEWRmkF-dMvHKKJRzdx890FMpCI-kRJuJ1QQvUZBO1BwAl6SuxdLsBBaz43eOmDdYVxY65xouTmY7Xa-myEMwXBb_PVcYgWUvZn0U1yAqJy0Q2WuHr755LoRVT7TGc4-rJKIVmT7428vNx0I5U8TKK4MeZoiLTrnYhAbaVtk2JlHhBuJHX6GRvCwMoY3zoq5csFFGz03ekoCsF21UBo6AhJXP13N0FRldEwOpT_w1MB3EvuHntmc_KLT7laeWNQXJkLLACqmB3BWDPKvsV-m6OPstTpS9lgTIxIet9wppqS-rFsg1fnWwmetbmWimXtfG6rSt3ZdPF2msH3JW7q9znqKOxSjKEJcxhPqr9Ly-q9iVzYjbRAVRc44RVNP74nZWGN1reO2vLaQySymG4pPS1pbPticuoJi3XDRYtiwlwOwTU94NTwASe8XU2viQk-vBzgbJkeCSHlOSwCxlpbXXRI4ilzkGSqetIRJ1TTceQeVQ9hcYEweu84zs80mBCtYGUi-wNvKjqgf_lgWX24A4JWvf9DqoFapby596Q9KIQOUf9qv86I1EmCM6CupMjX7AnAKTNCblEhjLhvXeHPJugO544YCmbJP_zYEc9I3Dm6bY16BRe3lhCt_lp_LYTYBWqohuJWX7p6a5PgcfgBv02NRFvYgfDxSa7kc9z-cc3K1ZNHIXWBdnnAAHhj-GdJkvbCjFbXAlZMz-Xauw1u4zne30m5LiDIO0f_xcX17p4RLpDGFGJT3rUrIelhiUEBLhkfIYoyBYawNNOXjyE4zCKv_fx3tKZQv0WJVNPVyA7FJTzvuaOu_MxUwA00rI5MRjr3YFns1YhShBvNGy1e4iJzDjg.r-uFKoX98eDPwU3KxKwfzw\",\"location\":\"https://api.vaxx.link/api/shl/KTseQR32kkQ4Q04aIjM1niOuxDOn1jZ_vYQb5jfIqCs/file/01ZLaUE74tElY96hbNvSOTvtzHI1_b2I6QnjZSCvcAI?ticket=jxTl7iwLe8_f3gmlk2MnlUIc5ZaPwtGpOeUb--nlewM\"},{\"contentType\":\"application/smart-health-card\",\"embedded\":\"eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIiwiY29udGVudFR5cGUiOiJhcHBsaWNhdGlvbi9zbWFydC1oZWFsdGgtY2FyZCJ9..5MKjktiUcrBKcPpt.UW5vpVfA1We34IWupDOgqQm5jM6T-bisa9m_A9VNZGLxQ67TXOzb6Azsx8rZo4_k35HoYKzn5vt8ergVz9ojBTVMmTMJYkJAv2mqc81Q2QZ1QxW9OsbtlTa4g43_g5wPD3DSpCYYEYfxBfP3TbjF5Au3ScCkahlFrAvtlataTvjRa9z4LTI3iG43YIBHtIBAS71rHg1mNgPn-C6oXIqJz09XwHC26YYqZWDYrcRYL9ceOiuGCg-d8K-sqgemgWGtGNP8mh-m5ILuSy4h1IzkKPBtIJAnfIiVOHbxpTpGWPKiFpp5Wf8OEtd6pO9t3iHjhCaamFN9-1xqFPR-zpJtO7TnZz0UO-WpSD8CDZQAnUPNHKhjad4zeECuW4SWQgCHXVBYXO40Kd1VtZCyvHkONfQ1R7d4NweEgOg-Ak11wW39hs3Drz7Jmo1cmr8xOq2T8EwodAXuIVyTb7Zr7d0yAv6adqfjGKhl7qskDDP6Dr7jWPwAdnejz3dgmkPUE8a7RVLHHkUA5jo_q7xNbqSYOpX4G_3kPCNTdg7ilLqU6pZacojRaUKdIz_pIuzYo4kEA8RLZekBbTG6qzdOuxxbWjd9U_x3JB0HeOfHot5e8D3QfHjhH0uceN3iiy9b1xnmhfens3me6YyHfu8cg0BcCMXWFVsC8NpF-pUQTK1ccGk4mlACsQ3OxJoqo2_k3YuzYGnzfWUemh3t5kRXGUw3M1I95-91Ypftes6DxT6UrXJkysHHfYVWc1YI6FhcfOyJs267wvD_Ph81IUVaA7z41J8SsQqT8ik0s6XvPU0gBUHX5u21GsD1Z7V0zs3jFeV4MDfFQlwlHgEb2jp_rpXiqUjwBCh4PXodZ7kLYQRQjRCVvBMUke5qrDbHFSEhr4ew-DCHrgW6e_N9Coi3qkhHWBLI0Q58PDOpTrvEUvg0gwY1Wh72Spu_7ESVZ7fUGT-pAXCX2Sd_3FBe7VjpBmhMtkT0RaTF1wRoZURPPv8xPDjGr8M.w70538E5gWakY9AwIHfDCw\",\"location\":\"https://api.vaxx.link/api/shl/KTseQR32kkQ4Q04aIjM1niOuxDOn1jZ_vYQb5jfIqCs/file/i8rFmdPKS7INteoI0K99ixLonloBq9cMmQ8hIPtJ2u4?ticket=jxTl7iwLe8_f3gmlk2MnlUIc5ZaPwtGpOeUb--nlewM\"},{\"contentType\":\"application/smart-health-card\",\"embedded\":\"eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIiwiY29udGVudFR5cGUiOiJhcHBsaWNhdGlvbi9zbWFydC1oZWFsdGgtY2FyZCJ9..krRtataNLwGcvjyr.DnZZm7ZSZi8_g_D8w-6oNifvo455cjnGTFyN3Kp_mxvWkn1LJMYGf53J1-D-2GXW7wVR3OaDY7KsLjEW2BH-GfO0SqSCW4heVgFwowGT4mTIaKOFDy-__lShu8VFVIB2zdiKz65Vt7CcBDJL6TzXFLvyzpnkomkiOzLBc1PMFJiYbnbwm_b0PJyrR5RSTE7c6A0B4AeoOnvEC9bgs4OXGZlMvo5o1gbBtwlcvdNG7Dsl96yO8yvWSbORhftwTjuhjSmQMPkfwOodbFsCGjB03I6Y01b_NlUsmi2H5GCdhoek55y-ljFeu0odMouA8n-xgBPZPR_lbMIAtb836nNuc7ZEQxfSOl_ujkQT9CltfVuALw7TOXK9NiKtcXKFdW__-ys-tyH3EjPqdDQ1jeeU1zI_B5FYwOiqoXwHHS4tCwO85_d_iU9LtgmY1Hnuvwzi8d8vj8Gf5TWOPkmwLqnRUGlpMFPCDFyyaCdZu6kqo8BLE2BWe6Iea84VEO5zxKD-k17gpdskfV7T9L6Ni6EOa9oQGDKzugFaFpmaclPnh0SFxxQZhgYjY9lGu71pUv7w-XkGu_vLQt8SFoguijjA-tpwctdBVPVNk_mXzXLebq6xpI-RF3E85M2YlQGFcZIKmwdvoaE21w7TL5PGHAftPSioQ_K7bJGEsi5UmBEen8vzsC3h4JTIpK3RsSQjGzh7rTBlEJN6VHfRgWB0lXUY3GCM9bwzygWqWC-zo_orjDX7VTIPuu4yfYijldenls884SZYcCZcsbJQOPTQzaIo98KM2lkBZHAxaliWy_XO0Ro7xxLeT91JemRdUYtwfxU8e-de_a7UAbTRXbczt0yLRViIGE5uHd16R8NKF_jgVL1QzH-ZrygUNzTphZqMzTc7xvLgSRqgzfpXxWSddVPh1kSNafWj3lk81skO8PdzJdLSONnbVhjUtmcD0w5EqRdE50RsXD-R9OfPk6_2CHUyYxdUKHnjVG--rl95omQf1H7pKg.cUQ9OaSEnthTvfEj1jj4vQ\",\"location\":\"https://api.vaxx.link/api/shl/KTseQR32kkQ4Q04aIjM1niOuxDOn1jZ_vYQb5jfIqCs/file/57F9RZi3XfDyDUkOt36Jw6GM5OnKx83Qi_TuA5r21Xo?ticket=jxTl7iwLe8_f3gmlk2MnlUIc5ZaPwtGpOeUb--nlewM\"},{\"contentType\":\"application/smart-health-card\",\"embedded\":\"eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIiwiY29udGVudFR5cGUiOiJhcHBsaWNhdGlvbi9zbWFydC1oZWFsdGgtY2FyZCJ9..-4Y1-6Dq_02A8MyG.cxfW510pp_vfpP_IXWfzhdCnxRBnOsvsXgUz76iyIFas_hMR1WA2-kkFxjhmK0M9mQMnf4u7j_1GPOlRwVLEtFJ04M3vPF5CZ7FCXK8ved_yDhe4HbFody6CLlrfHp1KMw8E_PaCq1VCR_lXdk2YsSLGRiO2pqTj12zdIYCzjylsbslJ7N-kfxgtE8pikPa4Vx_bHkiMAuHAMiW_ZTtsIFaYx2NVwT4nZDDbU-6oI9uhVUv3TQXqAypWgJcesfEK4x5kM_UutF8VDej8On4UIK2nenI5N5ENLoBtApVDaIbDQ6DnlnWf57QtOBv3p_4tAp5F4wsK9P8tHlWAg4CkJjYBX7GPP3kpCTnDRp7QwBw8SW1vctYy8lDTpPXSSC3fEaPFhiDrattzotPntR3G47HL_sGlOTnVMJ7Qv_PpHn9jDURzh0U9nW3wQWuMG-xakQddl2wAEqpXhpOcmTgwgPHqKSg7HiyOKYKpY8b6c_e9KsZlAUSnCoe15aQgPePd0agPHSkMOISY_Xep4cnNX2b1HFFUznHhFNSz8CfRUFAiAGdagQo-KaNu5ix-UVAuRFpnsAMIiuqzF2sTMaGNFwgv_F14drPWCfy_ec7w-w_X3-eQV045qnwatC6jyco6iWy7xSYQ9W5bSVc26Jh-wC1BREXXwozkS4h3GtGpqe_GKmCZqmhsSDW-wo4L9yfa64EXknPekKJbz8xDkad_N_vYxtjTIKBVywno_DYkhTMB_1FiPGiVTnSMj_KfHUgTbXvc8cFgd2St6RiINZqGIfTb63uVxIYLDrxGpkxVyda-gsZfBF5F0mixlp6ALQu1In2V0Ilbd6qoLm8OSOdr-2l0CxHMh6qWRjaqCVSzW6brJ29L8cvn7TnjVMKl1on0GNyNNr04AWADLU1geNFj1KpIZ29vmC-BcnEODfI09BTohYiHcOyHskJ7eswaYQ412-WqOSKeGiQuJYAgz2TPk3-NYUBJaORTLy16hQwrjDCAmw.ZEE9Qm8WJ-tUtvPFgXyrkA\",\"location\":\"https://api.vaxx.link/api/shl/KTseQR32kkQ4Q04aIjM1niOuxDOn1jZ_vYQb5jfIqCs/file/V4vQ7TttAP1fXq4piP8JxDyAc36pGLROP4vvnu-d6Bk?ticket=jxTl7iwLe8_f3gmlk2MnlUIc5ZaPwtGpOeUb--nlewM\"},{\"contentType\":\"application/smart-health-card\",\"embedded\":\"eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIiwiY29udGVudFR5cGUiOiJhcHBsaWNhdGlvbi9zbWFydC1oZWFsdGgtY2FyZCJ9..CuHXIp5QapRLT-I-.3ArZtNMSzazFu0BmAuacHghEvjoWkPgocVY9nBG2132EWPL8paCUSs08QIk2GLSlMMto5AQ2UMhoBsuJmD9Pd9Q8lsTJAL5DTrJ4akGlQz3BKAo0ZU8Be9afRhBZTLzidxX4mwqiO3_OSRjfGkvmYOybOqj3mFX0ijzSUTehE6LQqZx9GC0zdX1yrRp0cMDsdP1ZJQQ2Ndzk3dHr61xh_XMCdwBDDQOYLaxdYmP67_XcXWtNkTTz18nVmALwT4ObniNWCyRU6YIuoBL33o3ZIKpyw22JT6zxBEfCf5SHQeJpdXeHuTGgwfM3-JrQ5y49MQWG1YGmRBx189CrzxAWu6l94cXRZs3jGA51CpWG2KMu9u6oAO_HnxeZeWU5wXRUW3CHFMm_YDtAsr7Uiz8iW4Quj6PrC5OX0Iwx_sSn6lr1afKycs29X-e4UxBAGClJlxyP5-Ark1ZIf8oK2Q5S5LrwYd-xdMzUpadaqrUeT5_FHRqN9upRVM4d5tWRANjO5fcUA0Dw62-P8aOVBhXOkUk_XmAJXSVrQaMz7rjrza73ntJE6vMg55tZUezjrp1LH-4vrxVkFnCH9JLFuyl4VoLnrQHBpqYV6IReRnc-hmgmgvX7rVDRk1tnq5-SHlLLv8_Y1OYI5q7cX0LQdagZFKK_tEE_8bo3_j57Z6ol_WosywOyOvZycrBbF0Fd_NX6exN94jb42sB80rqPQkQ20AhtrJGuyrPdIcxavuBwHUK1gU1C48qo9Atacf3Y0ePj6HG_AIjx_-ZVA3wmTWrDXsw441fbIAOKBMhORJMdhqQN5YG5MRxfZnMVmFVgd4eCh-6z5MkaHlTXT90fSHEndze_CMr5yltyms6bX8eVAmmSEwlOjrmVX3MIswW73c1PjxYgNJrDdzI2LyeSUCMYzeBAHZD-HDiOUUusaEsfEHh1Sq3iH-dcfte8sxs4M_NSEX6eQNDcdmznch6BqHPe5khIO9nyU3LJtJ2_AcriOKoY9g.EW2ymsEjo6FVuq-hCGLuQw\",\"location\":\"https://api.vaxx.link/api/shl/KTseQR32kkQ4Q04aIjM1niOuxDOn1jZ_vYQb5jfIqCs/file/jaO41cMuv7u-1f9kdM4zT8bDTT74kLz_PS4Xxk61_-8?ticket=jxTl7iwLe8_f3gmlk2MnlUIc5ZaPwtGpOeUb--nlewM\"},{\"contentType\":\"application/smart-health-card\",\"embedded\":\"eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIiwiY29udGVudFR5cGUiOiJhcHBsaWNhdGlvbi9zbWFydC1oZWFsdGgtY2FyZCJ9..VIn-cCl2MzCTv1iF.2bSlcG-y9CO_7RfdmsHt_NXvYh3kuC2Fx70j6svKORmgough5qXkPIymYPXHqnStKTOvAs1zLR2J9R95iCZZ9WsLmZbSo_9akjAAzP-VkPBA1FoFVOlwf47JF8KhL2v4pMGI8fWgUWvjUDKRJrLjPuzHvjHeKfpsjiIBBZmgO6HxKbiGfb6-BsPhHPKXdVVUR7Xd0tc7yjQlDff4iy90pWcipGaStLhlDBhLXcVvuUjlO-tXZBQrozl1iVZk0Mx7VzCRy6XKvOdkerwYT_cLa3EG5W_kTAVbOCGcQbczUJpDMIlOrtO4qjL-BKo0x5pfVaxnBA3g9-3EhJzHjBrukEuBKGSCxiA2vGc3unzMUiYcYr7J7VUX27pmGmfwUtcz8LGMMVH3O2CHP5wtumz7W6UVgvNb7xlRBvI5AtP1fJLLr7VvV_twgFP1D6rO6vuqjhXaBPGTAwW7vdz_3d7khyDCWDXgMNOm3vOU8y-59btU5rtWk5LagLqqNN_4tpKmNsRT0oE_0bKhcluRYDLHyYCAKdpZr7l5WyHwSNQRcfL33RA84oT5ZiayNbyHqHPTRwJdmmdbcsr__oAvNm4qJt0QbNPq4AhuRc1iTtccoDTWq8G0b2dZrW0mHGh-Ky2Hmpj01Fcg07IhyWIEoRGJIsbq0fbNwkYcDjbMlGBaaJlw6-XO_7b2BiYnavIGGViH22GNVPTZJAzQVwzw9oFhuV0t-DEBFxco3CGMSbN-a-2Jbltgxbh5-2viFSG6H96XxcfeBrGkdtXNdzaYCRD4ej853r6o8roCVaXnA-FmOPMW5H9sfGEKoqdV5fqrnBbUwuA3OBZCXRZwpO73TXJ_4-bYAWAXnVhGMOUkkj3mUbAzVwjS6HLdaJcLVfpKnmEcK95WqjsGQprP2peKYv4_9zTanyRKnv6gWqdLS33Z1r9icNCYuJehg9EK3E1l6ye_CeJRUpjqyfM9W9lFmxr3v7TvGTSAfqDyOCALtrA.Bgxqka6vFx3ptGnbsfd-aA\",\"location\":\"https://api.vaxx.link/api/shl/KTseQR32kkQ4Q04aIjM1niOuxDOn1jZ_vYQb5jfIqCs/file/u0RVdZ5cGEevYAj8aO5CJxajok7hf5UjpBEbY-pIESw?ticket=jxTl7iwLe8_f3gmlk2MnlUIc5ZaPwtGpOeUb--nlewM\"},{\"contentType\":\"application/smart-health-card\",\"embedded\":\"eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIiwiY29udGVudFR5cGUiOiJhcHBsaWNhdGlvbi9zbWFydC1oZWFsdGgtY2FyZCJ9..2_L0LDitZpP0nlUZ.1pQIMEY7vvHk0J4GPijxROqszTbH3FTDrKqLOHbaQjCbEI12nA3kEZelJIiVHzcYt5sDG9sGubbroUHSLG9XzKIbjVXScoQwQyK_oh4-VQFmABaPEwOlA7F4VkF9gsJeRknCjWgUXkAf1r6F0LXNv-mA3tRhLx58EAbx-rEsvVZu3BJTrwz1NJu98cW6pnyHaZQvmTzBhEpY07YwooBNoHYEdXsJuEtJE29RzFhRu6NDzh4LGOe_idN_2hv6_-lBc-JEjYUnAmve5NeKH-6L9BXLzWXnR0MrkNea55zDchgdACySNd1nX4KeZSQo5rZydCt98FDJ9r4HDCT3D3RxhTmdmiBIUS3dNOzKZhab6bv4gT1mnaI3yMWTMSKCLMDEbhAw9nhESriW1sgF4Zhn3-KqF8Nyx_mzyWSAA-b7OUduZl9sRopbFODexknUrlSxTxwnIqKPQfUvCQMkGSysUBifQE1H3YPQro0fGvX6f4BROdJFJXV_hRARRXCG3au8Jv1aImn098vRcy_ENLcIdMX3uXqAEvOz4wXZ0bamn9rc0LdgUieiWJvAUK4bbJuv-qhhM1b7Xe7vEJElgyCjI6N4uh3Z4TaYihBLmdP6kNIX302Vx1Yc6qH1JVI8zkYEmcboIdOVAp4uP7NY4tFt243SvsV_ygfGpAgAzPPL05CBZJh-PdmZoZMsHJVNS-TbxrpBubkucjri6xdRY4UABGgK5GFtcYHbGTbrPTQXJ4hHEnjGXE8-BPN6rkFLoW06hpc9dadUrVupy1ueUAd9DOebgipyjojai3ZSZsYNj_y9TLUOWG5DE0FO_s7GQcfPnO_BnlOVxfPFc6pUjVlmmckemIb9I94_AbC2Ol6vtAs2rXQPmNgiV7j2ohV922Gz9FCuO4raQsMO4E5vtpXwdq_YZq7Fpnq8udHPV5AU4P4zUrIsRB_XFo3Yl7jrj1-jEhj0PaC1QQsobB0VCK3EwMgipNOfA2Go3HDpnqd6kwWEeL4.2Zmfea2rSA4bPHjj6Hknrg\",\"location\":\"https://api.vaxx.link/api/shl/KTseQR32kkQ4Q04aIjM1niOuxDOn1jZ_vYQb5jfIqCs/file/cfQth1z1aCspBBRDsC4WqwUq4MY5yk-FfwUHylG6tzM?ticket=jxTl7iwLe8_f3gmlk2MnlUIc5ZaPwtGpOeUb--nlewM\"},{\"contentType\":\"application/smart-health-card\",\"embedded\":\"eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIiwiY29udGVudFR5cGUiOiJhcHBsaWNhdGlvbi9zbWFydC1oZWFsdGgtY2FyZCJ9..JqFrZzTxJh1vNguY.lU8TnYSNhrmU2Gy7CIV3pyjcJyKC6OAKNrTNnpn4WdD6nTbMtOHIo6GpAy9Lq_Dh8xmLDfpjIAUQ6-6tEuv2mE-h2l6i6kKleZfBTJQBYuqMpP9F7Mo8IgX20pc4iwvMML-GCPYa4gAAs0sy9qpMFpwAD3qC1EehUxtbAvOS2v3Psi9wcubVGkms9epEWgemktX3aLzYgDkh-vE1c0ap7QMxKJOLCJsIGlPiDdefu6hh606nlchGJdD4nI7uLyEJKIbmlyjlAcRclZvOOoJMmFienNwXGHxKBCgRExabNpkIvsQaPpg64NG3JlxeSBlQq5RdCvzkwCznJ7h8raWPRMkJzFqEAUldDm91Lna1oGAaKndzjAtTn44dMZTyfHmCvcJGMlQmnLxGnFt0m_xyGczcylrliRYh1sSjkKxhR655DJuNByIpj8MAYuNeDw1mt0Sb-jDXuoMfvTkM0c_yt-2LsMqcVMbzd-sPIqcJz-yb8tWijCdhF3Ny6J86WnaGYSok9f9__G-5zswQjQxatnhasYLALt08GA0Y-d9tm7vaczIKddCDf9ptXXdzQhZkgjVfquqbzjBSj0SnFtWyCGWyiOO_VaR2DKqBHuzSXtDBzo0LkV80kbs0nEE08tJOY-SP5HrgCYLd6z-DOM3rwfOqcyN5XyvNwOPn9Dq1Ypg4oyPSXXCBKFzhsEC-4C9_bPPy8kyJS7IuGDF0vdsaM63mb1kOR8E4b9Qf9LHtdoQ538jr8jV50u-DKAT8L3Q8XSijvxphdSqlDO_7LBVy-6ZBaUg1cdCH5vb-pr3fQ5hULwLU5ULMjt5-NVIiSiVbMEReqJxHoUHCYyIBY-QJGlVUsGRjbHRxcRRdc0PqRRW5O4eYFeO-4thhP2gCwDq2CI9n4jwlRAJs6Zwxg96f9Z9t1ikCpTIDUlbU1bceyj1C6wbKTOOAbU2GrMHuwIyEkbkUSck7hGSeQ42_e8HUGJxnusGv1AbJK_tq3y37TcpHLwoTxA.--mLLVPP6gV0ZLAU6Tzm2w\",\"location\":\"https://api.vaxx.link/api/shl/KTseQR32kkQ4Q04aIjM1niOuxDOn1jZ_vYQb5jfIqCs/file/LNiNpDrxZCQHNWuWofBm3tv2Q4R_1AN05r3yhmnjJ6g?ticket=jxTl7iwLe8_f3gmlk2MnlUIc5ZaPwtGpOeUb--nlewM\"},{\"contentType\":\"application/smart-health-card\",\"embedded\":\"eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIiwiY29udGVudFR5cGUiOiJhcHBsaWNhdGlvbi9zbWFydC1oZWFsdGgtY2FyZCJ9..PHF1_ebHDJUTVF9S.ylh17DM3D-MyNU2j5NwNbO5vqvy9_DELEfbGq9HIzdIMyvClrWV_BDkhbdigxine4_4jol4IA5s_kp91xT8N6FnGh7fH1oWRZVUTRQ3e1T8xpVABXk-0oDEGtBu_W4QQx34szIPCiYT0gMCzNI2NGiyQl2g6BjQGhUxJfE-F5Dt2W_LQO2iqGmbs_C0R8xuyZQ6JHCwCeopfSrdDk50nTXydqalECi29zqnue6u4TzfCW24oIIHN7RcQrld_KYdg-L0O0djvF6dJmK1t_FnN3unFVA7hPmc18h92zNzftDSVBKmGvox6fdD27qXNfvOl4g_Fzq0Qw9r3Ml-b3UbOOF6kORC0I7TgNLrYlllRElLl9br1su0LwrGPO23t8klFX1gmOrJntyXynOZFVa84u4srD7hAUxvSEl6B9sUAgbPq_XTMin8WnaTkoQpBlLXDhdGWfwXTl6Vz0pbYpkrplSPeo0j0O-FTS9iFq4iEBy5GMg_haKNw_EaDCY53uT_tPb8tVsSxkR5lkqrr4uHycGFIDUZOwrUPhJPlan7DOEWu3ts_LyXHqShPZLmD_bmRACC4Lapo3obEfB6wUMuLCygUUdPjqa5XucBpjqphUywQBy6G9oWtMo0vPqPH6A9k3VW9Baj6IBdxNrpQKEJTL2L5CYFFC2o--ZaenEU_lXQR-rtD5nJrtrVyQWiAEAJ_SslpAuUXKt7TaVN9-6i3h_xmldQxegM2pmxzJAYUtWOfbhyEAU4CjKFwTp8w2cndohxu44tXR-wXy9e-F_-DcBNfvYOlEBKt15MpIhKko4zIL8-VtAxe7mXDNT4ZjKQV1lKezATJePNqzJ8QnSyUYOo9PTLmeRWE3dff_FNXwt4WEhMpVGggSoWo7Lvc2GLnUfW3ts7G3cIDbfukO3GG83kHjCPpBgT-lVOvcG_NDVI8bXZQNp6nRS8DVLAzJJMYDDhiAq3dBG33Y002Wz-FlbBGQPwzKtzTu41pMgmqNTNy_D8.tFqIm_FEGbT3mkEPkDlFtQ\",\"location\":\"https://api.vaxx.link/api/shl/KTseQR32kkQ4Q04aIjM1niOuxDOn1jZ_vYQb5jfIqCs/file/i-H-dcjyGjZo4qfJnWPzofAm5U585OkXvALXKR1Xi9Y?ticket=jxTl7iwLe8_f3gmlk2MnlUIc5ZaPwtGpOeUb--nlewM\"},{\"contentType\":\"application/smart-health-card\",\"embedded\":\"eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIiwiY29udGVudFR5cGUiOiJhcHBsaWNhdGlvbi9zbWFydC1oZWFsdGgtY2FyZCJ9..vJnZnc5CaRQRjVTH.fSanEekEUZIFIWRuadA6vD8R9e8Irov6A6vqLjc16HX2tumAQ9ll_kuyVuLahBy2BVWjfktQu5wZ8Ns6T46Eqywm7FfH4wKK9gS_QfBM-1yyKviiU72GEYyafTTkgnzJHlx_JbpX1Bh007AYoxT77ZiUTSY1FASrZSsbm478C7Fn3Dar03_r39-396MuDW1F7zWELjAzPlTsVNYSjDlAxWOFpRB7m06E2eDiphV4B1nQMOlqcq0JixU0n-2d4X4DhTs9KqVFXaDLX-YNWVMvfZGcFc8CgrQDqBOCpf9DqHmFQAYsfYgPy4I5fo753RKoZY6C69jfiOEhgSHUO0dxWc3ieKZEzjdZ-JQM2kbrvU89GJSScI_z0KkVXSk0st6ugjZ4CjX01ur3dITlo-90b32wIDN_jkOg3BuqJ5D9p0dyu4nCB3iTVx1x_5aWzG4-FoRt7byGDXM-_9UCpOTLOROn8AWjzSHv_dicq6cl3jNXDHMCHZ2nFubfabdTG0kkQYfQea2xWFaw2JEi-U6dgYBHjmuk6UjhJ9Rh08OU0JX-in6D0JBcrOrwFTj0-jVhTl0JwzPB4qfoZ8RZyQHB29TxLL-ByzaLNvFdNf_JvujcVM-Zfl-xlc64dG7gqHcGNPIwv-Pstm9Hq7NPcdcCwoDs8-WkxZ6WgodKesHuU_c5ELbxlNKhjZ6AUGiCVUzhrUBIUw8NJuJrqyp7Urh7j_wXtzXOSB9ohtfUqMjUe_6VPDydYdVTy0JUPA5PEP981FPyMd8WtImw3rqc1tj60E0g56uRVJCXeb_9d88A8oW-TUlrvJHpBnamhLYBwsfZ_w4-NdVdFoVFhD38HybnlrhHUPq9gxkieFezm1NU4Cu7rhdcR9BB0yOs1eio7XAZRk14gqct9gpF8bTFftR4pqhh4CsNaAYqPXFINtZJlQPMvWOb1snkp5QS7t63pKC2eVHs5H9Abz54zpZi0eYZsyZ9H2l9gvC_Ai9C9v_tYYdnTHbjFA.2B12Z3O9eZG1rQ7dczCKkw\",\"location\":\"https://api.vaxx.link/api/shl/KTseQR32kkQ4Q04aIjM1niOuxDOn1jZ_vYQb5jfIqCs/file/JttUai23_UyjfOEiQV0SyoHBErhTQOEvlH9jb4s3foM?ticket=jxTl7iwLe8_f3gmlk2MnlUIc5ZaPwtGpOeUb--nlewM\"},{\"contentType\":\"application/smart-health-card\",\"embedded\":\"eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIiwiY29udGVudFR5cGUiOiJhcHBsaWNhdGlvbi9zbWFydC1oZWFsdGgtY2FyZCJ9..cli8yBRs8JGZ2lxs.Ah-kTo9AEiMyWksXowyuDzukB03wVvWRO-hY1PVDGQdvbx6GrvBxl3Xx-xtbzs0_3HOME4RJahDOuX71ENWDB8AbXX1Bye4lTaC2xcXYM8dktY7sOGPuN-0z3Bq8DZVt9rk1o_qgLsObCWZVH5VqA7xxBPehcf0yw4xVcBWPljQVX_M0jbc54staywk8er4VMJRfRGSZrQHawShPbbCOdZpYZq1On-KrpP9_oVy1V3iVt-BSThjlqmvVE0Ln8Sm39trW11GE991KJRAqBHSk_A5dyOilP6wWmsvAIr05oWRHtIrWPPRX9Bf8FuD6AgBMHkGYxdJ59SFOOOJC4S3plu7RMHCjg24yP4JKihGAoYIgx8pE1Cl9KvVpKXpcCGMHDvHpX3u60gpC736qRErPo5PDlidxkQgOc9XHd04VdAzSco6RaNpRP4hQTfFm_2bN8R8_43q7LvpJmuK0hy6y4iS5zVALIBtleqCutR0dnUOrvr058Judt8a1T4ViBHEFlRov3Aho7pjh3g7tLFDMZraF8oQ6Q8F8TIvyVtFLe2cJFdJphc57U84lzEvc90agpwE4DZt1hr8qDO5mCld_nluw_JLgOFSZDHYE3EeRLMf54vzQmJX_qDTYAwaAg2KvPu8yH7pHJCAebUiUAuMF6cUy0wQb1TaPcubcmyBVw_WIqAy_lqEGNFekFtHVTepuELKly3ahg2J3TRCDQ9gefl_aoqwaaaDaiF4PGgiINOj_B90GBpRu4DJxFj245gRnxCusGzHdSrhHc-tt1Qr0TqRUUjdFnwuAHnyhgp5xGWG6k7QCkHDL6uGUmFIZVgXnlErVQSyUZtCHYZxVBxiAB-K6DQ4gcgzVGJ23SM5Uz2rRKauE07jQu9-3RaAt9QfyEXmrH3PcH6ApSPdrMnaa24vvfRBoGAMu-Q2HQCLi2DJ0j-Datk_dQmT8GFklTQGVyJOk6VZkuQqAdB8bP8luqinlxehZtoUoj34hQv_1EOCjJrZorrw.KAoLXJ7Co3p2gVCAoi0FWw\",\"location\":\"https://api.vaxx.link/api/shl/KTseQR32kkQ4Q04aIjM1niOuxDOn1jZ_vYQb5jfIqCs/file/zC24e_1TzQdp47czV2H22Mp8Vt18eklmkA_DsZy3Bjo?ticket=jxTl7iwLe8_f3gmlk2MnlUIc5ZaPwtGpOeUb--nlewM\"},{\"contentType\":\"application/smart-health-card\",\"embedded\":\"eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIiwiY29udGVudFR5cGUiOiJhcHBsaWNhdGlvbi9zbWFydC1oZWFsdGgtY2FyZCJ9..EjmhbQXKSsHn0sqR.Jq70n7wwr9l_i3GYOOEXggN3aT5Hm1ddkqWOQ9074vo32NfIHLcJ9dOJigZIf5gvCQem2yrwtuoDZ5te7XoP0W1ILbksEPbnDykaSjBn_vN6W27TiEdAkBz0oeSVBE1YaLQ_P9x4ER-GnSX7LqxTIaUGDqY1nEKTzzWZo41OA94CxZj_yY5c-rlwEY80RG7NvOZrObNuBDdtMZ8DDVyaY5ahdq3JU8AslemmHfPmr8GNsT1OPX4TNWnNstGmKl0f79kN9LiNoSCuu-zhaWysx7CzUSYwJkEv9FsSKRmTBUqyaBK3KbR5SGEHfyw0gpfP56GNkyL5xfCzabaHznIvaonXAR7vH946t1EFwEN-Aurw1WH9pquDaWfETA-EtYL8tlz2GTm2azhmUW35b6AmD-xrrHijBpifVSfd_MKxzAt6rjgEGFvOPDyNURJqKPUX9hzyXoUBzFQAC3RWQWj4zE-d7bHedjJkDGAe0sBm6eIL6uWgQIlgM6DS9p2ZA3UEKMz8iJLZJyIabIeoL5CHgCm7xFHjgD5_ounjJD_Z5PusaRguTMpG-ph2-g2wAJtWeulox_zhwKLf4OBoA05QIOXThzOUl8Mk7n4tvUmvsBSGKAGOcXcPSlaTbpVWiBsUCxpB-apTTq6A0knqGS2Y_Si2wCFm_2oJ3wW9DZnWNaWrfuUQJl_NO85dyR0zYqv9SqwDqBkJd3fLNMR0iAJPVvY3X7Txp_GfB5YQ_q2m9vESj_pVqzJDIVfvj6UnDEiPI9W9srSLbvKCJMwUWrsZvDkian86sgycv4b0ndqYaDn771n-CjjGHmfGucDovc4WaB60cMGeieqYz2Gjcyj2A_7HNVPFOVD9VFHMGO-fgI589XSBeVHYycPTk7PvuCO9V1GxB4TlXOy8Ui5NqZlaIOnll9aSjJrnq987wzGuqgQjzfVldBY4Xtl_Vmf_6A67GSjaXMMVPzP970KQB-o47FECp2i04s6SQ2eLE0ZEyu9B2_vJACQ.8e0ObVXqzJw65RlADNHhtw\",\"location\":\"https://api.vaxx.link/api/shl/KTseQR32kkQ4Q04aIjM1niOuxDOn1jZ_vYQb5jfIqCs/file/sT4DSbzVk4pP1M5hUD9HQuMaZ1ptvz_S9EC2sdaAksY?ticket=jxTl7iwLe8_f3gmlk2MnlUIc5ZaPwtGpOeUb--nlewM\"},{\"contentType\":\"application/smart-health-card\",\"embedded\":\"eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIiwiY29udGVudFR5cGUiOiJhcHBsaWNhdGlvbi9zbWFydC1oZWFsdGgtY2FyZCJ9..w7LKrVwuPm_mSLJk.zpEEOBtgY23wvBAwk3G6y0f8y3xR36-WBbBhtFQQZIgiFv3-wzBs37WbH3vV2Jijl7C7FnDcNU4EhJH2Apqx6o8eY3Zf9Vtp8EMzGg84T-GPzbnE6OANuolu6aa-n7vIX8sHGhHUYo8NYTGfnCgvWvG67vzsrCkfdvB0JJZmFenQrI3Eu1NTOPe4AmwdYt9_ZGHoRvU_Sjb9ykb_mstIFwDiaPH2QeUU0k0htj97lGekJNdaPxDa-pU27-3Se1ns1aU53gkao_s3JntGjzNQvL0Sa2btItxJHNN75M5Olw9-hyfi4TJChdRTCGFNwZevAdbNjtFSl5tj3E7qIZ1ZjFkVLw1kNyVdQHXxp2ZmLDc6LV1Nf5cQ1y1y-U-JRVrgnOuaV37RdEYdnbeSurB-TvT5ifzEJzWhJu0ISIZqb5wNuwJVuyqmCFHdWpVjALNGvb1vjBLkB9Q_vFaLh6iZg9MD1CmKbGY7vY8b49MEIfoAH2OLuPbACL0kecLXjBZTwTJv3kxHxw_HyApYEQzDgC1p_R7p6dU9fEIABuRPV4_kT8deB8TlZ-efl-Ykd77x6hotQZZu3Qk7DhBjaPxgeCXJE3v-X0Gy0w22Lym4ZgghG23AlDSou-0V0WdxWZf0MVKUlJYql9iHrKwUdICqt2LR9kqh6KetoFYcw0JUPGUiSHU_dabBfNqZ9U_16nuBx7WOUJLIS4Ycp3vbCXTAwKxstekT9Y0H-eV2qViDDltJwpezubgbQjIMFlZ4LDrNE0f2hKDMh0mlhmMey_q8ndHZMfMLZMWdoRNbvdOmecdAEyrG29P31KvI68iWsq8FD-BIqrohgA0ZbMFUldVVDsQzpH5CM327J1aO7ufaBVKMsyHLojZpFt-OLcX1_AnZDMBJPNqBiBwQ8CCZ-KiTKhwek_RbP9FuzSp5Xke5mMESL5ARJirj3LrYgCR83lyn0t8QzHQdgBx_Y_99gjfoZJWvLCZG-VgpRqeqbZwqqKF4dg.SO255ra5Az8XhVVLfqN7VQ\",\"location\":\"https://api.vaxx.link/api/shl/KTseQR32kkQ4Q04aIjM1niOuxDOn1jZ_vYQb5jfIqCs/file/hUMkPWJ-hCgKm8NwwOmvsoViCH9J6hucExZ4UeXEBgg?ticket=jxTl7iwLe8_f3gmlk2MnlUIc5ZaPwtGpOeUb--nlewM\"},{\"contentType\":\"application/smart-health-card\",\"embedded\":\"eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIiwiY29udGVudFR5cGUiOiJhcHBsaWNhdGlvbi9zbWFydC1oZWFsdGgtY2FyZCJ9..nrM9xQISQE577zZF.B7j__qp1t5uksWhlTbI-VeKiTEGFTdMqqRIdkKnwUU6vgo7JJwB1NQavC3EiIPYQQD6GbsUBTq9RjNDrKryaiVBrtZU97ska1IiEcNYqldLL2FYf5OkCdSHG5gz3-FbbMqpMLN2ZmUrly7ZNR0pdjzWLdrVQS6AFDLuims5hwqJrVJdHPVXhQn2Pl5UT-hug0VpnkU3i_mg5R-OVbxTSeEwq_L0wUZfrlZCRAqqOIcQtVXscvfIlQJRg_kEsMZNi8lBPSIOYhJBQAAIjKjMvaUlHV_nLme-jWHF0c3d4UTWJFxIA6HyIjltcFjwwzPzcHma50s0cUQh7d7Wdpx4DY7c2rVyw96u2koznUOhyBFnEa4yF6D2V3FRNb1Wx-hCY48AYsdicE7StF-zTkjANTMkEGW2ePJfzvCQNUfEdgFBMIGksqjTDlQKpjFICXGUsKW7we5MiB0ELkfjUa8zXks9Y4QtSmvEACu25Uq2x3vvfn-DDJY3PeIYAonX-giCE9-YRPjtQU5bGDWhpbYsVzxbpYU27TAPkO6YgP50S98KNdmC20bvuO50zgAc7nOI2X2mx9wZTgtChcFiH5mOKCG5hFAqhPRJVXpRcPVmUx8WEnMztyJB5YYTVwnTh5FFLqUnEdbZ2zjI1CAbY7VxNyN09blCwkRdAjl_uRp9X26V-nJ6BI28tpH6NhzJQkMKWYiZs3cJZDYtp0wBOqNfFzgOhPyg7iNvPfHV9WBd584FzblRr7ewZUMrJOWtBevtpd6raPibQdpnRUleI-8e3Qe7XLw-zT3iyi5aA2cEmTPVXsSreSM-IcTPFH9jm4mw_42QDaC3CLGQP9ymVDHv6CgfWqfE9HCb6D6S3AqfoJZK-3FK2OmmzLOY18kJAeMGi-TJFMye1aWUDXMYvMb82k1OfantIWJ-3xk2ZxBhrRAhbktf9gkFFPxgXrv-ckTjPK2FrfHzLSGpQ9l2ieBW_37AvcpsLV9xqJc5EkLUVYapoNWM.MaWQVurWBeYj08o4l8Mohw\",\"location\":\"https://api.vaxx.link/api/shl/KTseQR32kkQ4Q04aIjM1niOuxDOn1jZ_vYQb5jfIqCs/file/CTqN8ZSfxD0L2V7ybEHhT5yA-e0CjMPnNs3b-jlvv-g?ticket=jxTl7iwLe8_f3gmlk2MnlUIc5ZaPwtGpOeUb--nlewM\"},{\"contentType\":\"application/smart-health-card\",\"embedded\":\"eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIiwiY29udGVudFR5cGUiOiJhcHBsaWNhdGlvbi9zbWFydC1oZWFsdGgtY2FyZCJ9..BddSd1FY5uX3usCL.h26DDk365cOHmHCK_oI8kEujkfqyxJ8wE-BcsCNHQKj1HPiJEyL4bij-5opqJZ4sLxyXeOhwyhcaSsUp9i_DK2bw5RkFAn3_2TzDfUfcLKFxHhAhrLU7Cmujki_pyNe7N5YSU7glYGcb8cOBueK5hN7CLrqlMM6qW2-uYTc9e8l9r7Hi5w4H-5sjZtM-XNrvWRQcWfAPblOdoqDZUmYsuKjU_tf1Q7EvkDmvix2nVAQgAOa41cMyPG_FsrHnOx7VT5cFSgnLQjfy9n37Ka3KzSg4EAhyICGRrEJYEQWvfWV5drxKk6pKK-YJM-iRMrAhRPp771LL4vi8uqGOIlm9JF0QSbikI-3zf1IpVW9wzHrTmpyFe-mzUt9SKZnjBug86PIEZzi-_suVn454MaIM8ooD9Ghz6DvDxPMABE8A1OQ3A50z-2Dsw6188R1rauok1z34wpUdAKGNan1blAKUxNLLLNG3JNKdYiJ8hkqjH_w0h0WzI8-RTUsBFLzCh3HIBWoZHnJ7RHibtTG7VlgI1sTGQT0YsiSml6UGIv2RPShP1Dy0SK-lDnh1ugYBMWgIfpx2Ehh8yjdK2VMewWTaTrZ_-hzJwF86PRNqu_SJodd6CIuvJnC3eFRn1MjdQNqOUlgg3WcBlybp7teZQCw7qFOLRbAO-KQFsvmrgahVsyc1JchaWZKgogWUq5Xsii6MP6-fR6DRcP4ri4cdW1rTzLVRmhpY8r9y0KxLKJHmefKrL9Y7cgLOFIjEl12SAQ2AJu2MboFdouNXogrWNp5ezBkCc5y_-DBDPPmRsS3fORw2gNOrHqr0gj9Sqf9tcWkm0X_pfnFU3efVFk9CGIDv_hyviuUr8gtw4Ms7RpZAnX9JIhlI078V2Oj8mlxYSNM3RuAMDdyqp8QLW8Ht4XsQ93j-XKszXS28_rNlR8TsKODuhMXYkOLQknKWxmeHtQBya3Rrp-hXP6n_MMqedtUxd9yAx-Z3yBXYZ6IZD2Ee3TCy_gtOfg.5sheYRqpjRBHfmaNi1xtWw\",\"location\":\"https://api.vaxx.link/api/shl/KTseQR32kkQ4Q04aIjM1niOuxDOn1jZ_vYQb5jfIqCs/file/M-zlcnn2W0Ou4wp44EvcJYrrxO-oc7uGFFYs4jmniqk?ticket=jxTl7iwLe8_f3gmlk2MnlUIc5ZaPwtGpOeUb--nlewM\"},{\"contentType\":\"application/smart-health-card\",\"embedded\":\"eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIiwiY29udGVudFR5cGUiOiJhcHBsaWNhdGlvbi9zbWFydC1oZWFsdGgtY2FyZCJ9..vb98yy3PUajKpqHV.6Oem9nEAxRA-qLRJm-pjcgfGAcBIEOAcDKlSjDIeA7Jxd2nNoafwJrSVH-XvNg9sgJFWYpai6s9cQbynzijiqCpbnU5juSWQwJMVWiIONAKzuCDECn3h1Btq5CxXGvDJUZkfn89VMi6rMBaZSIurgLdfl-3A4ZXZ1YvEH15ajBPFl3rHOOn0VdL6_Y2wbvhw9U8TmzIwMd2cguem5ULzl8YiPkzdMymMZFS0B-c-xbXYmVLj0fuEPpVq53rX5wcu1AHhX6sYAaeeyN-1Zs0KZjb8e2TzAj33p2CdOPNN6AkMtF5pTejjYZcb8Ww7g0aHTOyh5my7zQYs786mPMdntjA9jo5exZFFS07jVyFWYTN4ho79Ify4YIU51vXBxkRRSUnVFBE6paWqqCXCMjxGzf1Z8dpgwlw8SvPSAMwmhkEPPQfxr7FXIBE2iwGwLJ4qhlrSUR5Ep4ZWAGyGD1WfEaCJQyO_m0K6PXWOot1kDZyDzVb0YOj_GLJbo33VAYuxD6ttBiGuyPfIBSMOn9TmJ624VzOXu9GWRdZvqDVP_waJoiZBfxbOq4WvZ-wJC0VHWE2rhhuOG8KtxrgbxYmd7_Oc7p2DaTo9vNDPmXt8GoQBPkeBEpZyBR56yIzITkXUZ2HLfit261N1v7bxikRKKH8YXkQPuRwzZzNPLHr81rxN8jjxawJOX5Pj1FcwhSEVUKpqHAXapYQsyhO9cuHsiAEyJ3emuSMmlMrZ8ZGzZZiWICvAE_UzpPQZ-ROKFUvvkIqX5AHV51WD52kC5CqyXYQ4KxsPk_BBlBf-HmKyNz6-PSiHLZZrBo-BK7PLCrDpGf6nAXoNHYHVvaZkuN4UyxMb4YZbVfLkZaiMGd4mUPwUs1Wf2emTRQrW3OdM2XGr7wKESu83h4b8Dwzq8wLYqnRXlDzSX3NE2_lSeEVzbZePipynuvvRobokZmmA78rf_q13q3py5ABCzsHKSpij31cD-6R1-JZjNKmalinePIoq77Y.rhvIYMSABaPan_BQoUR5DA\",\"location\":\"https://api.vaxx.link/api/shl/KTseQR32kkQ4Q04aIjM1niOuxDOn1jZ_vYQb5jfIqCs/file/ftH39DmpvmX1ql4fXnS5xB0kj8dIktjRpYoB4D33WyY?ticket=jxTl7iwLe8_f3gmlk2MnlUIc5ZaPwtGpOeUb--nlewM\"},{\"contentType\":\"application/smart-health-card\",\"embedded\":\"eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIiwiY29udGVudFR5cGUiOiJhcHBsaWNhdGlvbi9zbWFydC1oZWFsdGgtY2FyZCJ9..9mSY64oj-VPMddpV.t3hrgofdsQMUpQEY-nZvuFWE8cYj3mdwDZhErno0TuIfwuwWGi9nkvvv7beZ-Cz1NDbyYPuopET348EGcL571IbjP4eWWlnb1mj4anAaNqaxegT6Ed33mb_1bX9UtRKdRZs_DIkKGwgpba5r5LWOj9sAYAVXKSkT6vRZBTynQ0YIW53-gmJo7h2dgyRqPOhcHtVxIUfbUa4rarL5S30wUMx_aC6FC2v9-MwuCikh5usSfG2yOgzMPWGb8fOVQRLEHW7s8SwEzRhv2UzlDKRD4CppBz-Lie4tLXV_CifKYtkPa60W7f20UP5RhxYAT2qIejM3aL5AebxFimqZyK7B7oluL62BS0Djbd6FM0hqwXjsf2k7ZL5PY7sH3-uj1fyejwZbk3sBMzdRV7l7CgqzvsItZMhCXarrCCepS1Z6PY36DR28kzSt85G_shzTssf7i8LXg-C8kUqUaSnADdCrk_mjRSd-vEEr_XIj45owGhjxmGa_8Uhs_TicFUzQ07_qTsXy7LHN1f0uLE75cMgAgBKqJlYtKonlrHh6SSB7gf-LJf2Dz58jbf_KlwrE4mO2FnhvaLEKDPCJjPCul4iMQkw5AssGqEze-gscA3gyFrkSqRSYiXrLTmDnjC9FRelDj9GEEQSPOKyViflPRY-6CFd5qBP4zwhiMKkZ-vP-IuoBddQSg4S-M8qtLbFUpBf2vGe8iXh-13ydTP66TG_bHM5gCmdfPuavOXRkUhm15Dq4704EyzdRyvcj5-6U388FKTV_GnCSexuv5O6Se_aLf6ZBKw02EyvcXSOvPsCmoMx5jMKoVTe26AIxnNTFXxI_2-zppQn7bVYRcFmolC90MMatSw03S1oGP7mjtvWPckLJUhKlqmTjNC4n_2-Sh2wGkcSSdjd5SQSvbni95Sh8MyYFM2nxq5O6sUKLu0v8W3xEMMvaMFxCRgZegcWjLLK02XI7RV7Bae7kg2zVyDkMgHU63SkPC8gYKOnhHQwr7ZCM7Nnaiw.y8n5Of8HAPKFG_Br6PadBg\",\"location\":\"https://api.vaxx.link/api/shl/KTseQR32kkQ4Q04aIjM1niOuxDOn1jZ_vYQb5jfIqCs/file/7q-Pkcekjn0XY9FQAfmQQIui2W2puvf1SV7oDQB2EWg?ticket=jxTl7iwLe8_f3gmlk2MnlUIc5ZaPwtGpOeUb--nlewM\"},{\"contentType\":\"application/smart-health-card\",\"embedded\":\"eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIiwiY29udGVudFR5cGUiOiJhcHBsaWNhdGlvbi9zbWFydC1oZWFsdGgtY2FyZCJ9..ih6ZUJ6a3O1ACrAi.qjdyzrhWd67KRIJ1ON7sAZEnPaBo2hFBGzmkBOKUmJsZrEagJU0cr3HxFA5UDVbrnFZCy0byVjE7EH9_0Jq_sOiPogGGP5GBZblDnq053xrpUrV-VfLGaB5dmvEKPzF-OEJrdWjyPfvED2VofLPOwhOLnDqfclDPkXZLT53jkPF0WXov1dzjVkY3bcuKhI43Vi6PJiyLRzP36dwTM8mqn16BB2hLdHfQMxQvPg8JHUKargG3UodPPTy4YOoWEvoPCpRv7-3ocIOgnIJt14GMxQJZb_c18i_qNYVgHl5gW0Dg5M2Ijb4TYDkB1H4f6KlaPR1oaXgqXBt8WhNJlfOn9qCM1UZFFc01hcM39WlX69xuhIsPrS1SvWj6ZxgvJ5XZ61_mzXgCsmM5_r5HrgdcgLPUFQUVhcVMfAc0RNSclWIcHKxSLZ58lcR04VVbCAoZhN0uj-PsiphP40hrOy5giQr6ChR44H02JtIwAoQzqW20GFkzRTElvDosGcia0DRg35cj5lBPel8fy_NlId7jPrar6cqQ6BVxBB66vxkTl5Cylq6uYnd5OJzQBR2Rd5ZVX1-gyrLIVpmq0EGWfxvzc9CTi3qFJIlpHtH2f9s1x0YT9iRe1BLapPx-IOW9FkDiUqzsWsAjhwv-iw5OvAshX4xhXQiNLo1l74Cp6ZxZTyGyWEXkLMdbbUrg_PTt3mNPmguFEgAVdIsB-Uvw1UbjUh7T8TJ_IIRcgRsovAQFxZuqjdgP0ow_lE5kUiOlLhbwfcNgN9FVjTVxSrIoWTuCsFHNDWA_tqZx7lUhhoFcvIZofdoj2Ue28VBbSCLfB3Binlql6qYh5sg8pjufD_dbWS6dzgXqCCG-Q-SIm3qzCIu5WOCB2xhjbNQ5JKMd79yV_ickvlw6DA2Uwrkx-RdH0_iyL_R_yHPFwZ3xj-oW0j5_NhrCyFptaW-q29xnYQ38hhjKokr4wUw0h_zdniX6_O0YdZkHYez1LzwknNqwkH0btKbfOgA.KvU0Nq0me8_yH9AX1PjwBA\",\"location\":\"https://api.vaxx.link/api/shl/KTseQR32kkQ4Q04aIjM1niOuxDOn1jZ_vYQb5jfIqCs/file/vNLjEpc1w7MmITCPUDZxGY81bSCRHXaN-UBAECVg8Vw?ticket=jxTl7iwLe8_f3gmlk2MnlUIc5ZaPwtGpOeUb--nlewM\"}]}"

        //val decompressedPayload = decodePayload(response)
        // System.out.println(decompressedPayload)


        //println(arr[0])

        // val jwe = JsonWebEncryption()
        // jwe.compactSerialization = jwt
        // jwe.key = key
        //
        // val payload = jwe.plaintextString
        // println("Payload: $payload")
    }

    @Test
    fun inflate() {
        // val str = "pZJJT8MwEIX_ChquaZZSthyBAxwQSCwX1IPrTBsjL9HYKRSU_85MaAVCiAtSDnH85vN7z3kHEyPU0KbUxbooYoc6j05RalHZ1OZaURMLfFWusxgLVvdIkIFfLKGujman5bQ8mM7y6dFhBmsN9TukTYdQP30xf-L2PxcTWTDq_zrjXO_Nm0omeJhnoAkb9Mkoe9cvnlEnsbVsDT0iRdHUMMvLvGKofD3rfWNRNIQx9KTxfowA241sGwl0sJZpQsiAD6AN52Ryb-0DWRbs5uuSBbvFL-Bbtsrz0qNy-AlRztiNHErhRfgrs0YvPd5YfiOYD5xsYTj6hUoCmZbV8aSsJuUMhiH71Ub1t42r771lEJNKfRxzym0nlNbXSmvj8Tw0I0GHxvjV6DhuYkK3_Xn4Xlp7nAdaFVJpEU1T6PUrA_Q4CeUJDPMhg24bfXSzREIv1r43x6KgdU_jlmS9N-5HXsYgLQM57kWsKJ0CCbIxsbNKarxGMglp7zLEziRluaP5-AzDBw\n"
        // println(file2.length)
    }

    @Test
    fun encodeAndCompress() {
        println(urlUtilsMock.encodeAndCompressPayload(file))
    }

    @Test
    fun testManifestPost() {
        val res = urlUtilsMock.getManifestUrl()
        println(res)
    }


}