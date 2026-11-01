package gr.makrygenis.anastasia

/** Oi listes tis Anastasias, opos einai sto playlists.txt tou PC. */
data class Kategoria(val kleidi: String, val onoma: String, val varos: Int, val listes: List<String>)

object Listes {
    val oles: List<Kategoria> = listOf(
        Kategoria("Ellinika", "Ελληνικά", 5, listOf(
            "RDn5UqxdSgon8", "PLecvcgv22qrA6Sv7KgT4TrOXtEO_jJSPV", "RDGMEM5r_-Twxt33WMERowgp5Y7AVMdZdbtfiD49E", "RD9hrLXnBIkLo", "PLj-Qpp3vLqCNX4khXRNmc1MwbkJjRHSGe", "RDEM-0t0SETjf8qCKbnsX11dwg", "RDEMdNogBGmCgJLMUKzaxd6GBg", "PLyaYP0A9OqHFXvxkWUvGtSanUjkuoeMF0", "PLGHEv8f9S5up901GVx_-rbiGtyObhekDY", "PLwG1Won5kGQPsLyP_oocKY_fQq11xsu4x", "PL0Ry_4wKYlHNz_7j8NbMdXRr2it_RfH9O", "PL2OVNHvyOqggmGtUfQHuTIPzGWEl526CO", "PLwG1Won5kGQNvMIrVdpGe7QqQPBoJd3eT", "PLzWICWq8I2UEiM6_vbaVbZ7L-MRpaHqsL", "RDEMAZQaE9BYk1UTzL0wAb4S4w", "RDfWpKt-Lfa0o", "RDGMEMQ1dJ7wXfLlqCjwV0xfSNbA", "RDRkO3c7ej2YI", "PLU2l0QidNM0Tjz7dtgkOTo-EIOvbO9Lw7", "PLb8X-mhB4AD96AiKuUSLum6ZkfE39dI7h", "PLXOFYCZ8VYqb1-8uDKlS4xl15-Abq3fnQ", "PLR1pV08XJLn62PKSpZQ9dMDkdOczA0Mrx", "RD_hBOZYrXSvo", "RD5Dq67sVUsVM", "RDcXqeaDoe7Pk", "RD8UPQUzhtnLA", "RDG1ltM4fPzjA", "RDZzH62xP8L3o"
        )),
        Kategoria("Ellinika_80s_90s", "Ελληνικά 80s/90s", 2, listOf(
            "PLb3nVgLBZrtsgTcVEOGyz_VQxuFd3fKFQ", "PLqxRqXoYOYkhhGmgoCBEcCBgZtcuwquuh", "PLTZfxNyQYbJSM2Emwp1a8LsQIXMq-rlL_", "PLOfTMnVpL5RAbrQCeepQiIqvDNcwUG6fq", "PL-PzYwZweLry5_QNtZINO0SlM_-drJ8i2", "PLNecoky1fw2grhFqYS-i953Jj7lHkm3k1", "PLBks2d3pE8qS_Pg6bFe5Xf1c1rFdL6wCU", "PLCIkhNwvt2U3DCUXyTQ6Uc8AjcB7iXI_R", "PL1FD46559D7E3DA94", "PLjbewq2I0Uq9qvZRP6h38_gkZV4tG-aMl", "PL30iPmtEANScSBklgE-rE5vKjf2W0rj_5", "PLA0794EB46E7173F0", "PL66A22A7D300BC2D7", "PLKmcbSmnk7wV_pBCvN8yLShudSrVoWmkZ", "PLefxq8UdmsVNS7A9-7AMPAZww3cln9GD2"
        )),
        Kategoria("Xena", "Ξένα", 1, listOf(
            "PLSR4G45zEhnYtohBmkREKp6L2C8vufc5P", "PLvispH3lXLv9V76vRPnLMY2xs3CoJQISF", "PLdxeTSn47T2m-lnaGmKqpJaUQkrw4ciSn", "PL8E7B712F47E82E42", "PLX8RLn_hcPPHQQaxT7B6nlwEX5NGJlX4v", "PLduwZxZ72L8MSY1awqggDPDatdYc0wW4j", "PLONqtwi88v_7soxzPisf3HrR5s5I3QfJi", "PL_adZhO9fB0vZY7g-mAEdlcqjr2rSxX25", "PLyf1eNAKjOw5Zi7OYxKDAmex-wqkwfVHx", "PLONqtwi88v_4IjNgjEhmo7t6tGzkN0osQ", "PLbmewxOKRnMVBvJ6UqE5OFriO5dYPXXUy", "PLbMjU_TVMIFvmkQ871iUvE5gy5ezXc6hE", "RDEMASZzAB4N6PSfsOwzAhxYyQ", "RDggtkyyWtQ44", "RDfJ9rUzIMcZQ", "RDlDK9QqIzhwk"
        )),
        Kategoria("Xena_80s_90s", "Ξένα 80s/90s", 1, listOf(
            "PLT7Df7SSalJOqdEe7rhtKKpdRBVs_CMhN", "PLT1RK1yTyxfj5ukBab9spmLvBXapxLYhq", "PLm8013_gmOkhPPLyfnk1P7_o3top0a1Z5", "PLr8hBhw_tOD5a0BIug06yzmcOXuI6UGkh", "PLO1va19ZyeARSI_QKxdnGSR1B10swc7BL", "PLQ5fknpm1NACCMpxEDKGHdtXSRYuWGV1r", "PLNAzQwj53YOzFdBtMPEI9rmO_vLAy7IsB", "PLNupbz3LoeADLv-_ebNoRJMQy6e4Kk16f", "RDmc3GP8gg4Eo", "RDQRoWiTcO7dk", "RDCkN8ypwX3nM", "RDjdwAXVHliqc"
        )),
        Kategoria("Paidika_Ellinika", "Παιδικά ελληνικά", 2, listOf(
            "PLJHo_18MeWSIWe3CpG1jdSELRON8OOYFP", "PLz80pS8PHRNDBuNQd7KFGyU83KLEULuNt", "PLCAEE43DD1956C11C", "PLRV_q8pFOjzk5yene2fRo5WOQho74fQL2", "PLMTeTdiHzryCf_nfEjrXNxVuFBVPtpQ7I", "PLEnZ8tRcrQFeAT42JOOoB02uUSXwXJg8_", "PL8NLAOg0UGegL3UVSmkhwoP9KWDjhEdfE", "PLWMdOkRzQYswZ3nJ6M6hret7Q1tY6GIe-", "PLthyxzm5T8bb2JstePOVBeN-hV_KizdK8", "PLWMdOkRzQYsy8EXgRswek5_bH4LuzHvVC", "PLWMdOkRzQYszxLyBOClrZuML-DAemazvl", "PLuzImx_hx39mGZJleCjV7q5PhGbVt5onE", "PLE6D20FD6A2A8DC1D", "PLecvcgv22qrDBzJgFM0anS2xJkWFP7vdx", "PL7vB-NYPBI7-2IXdqkcJfRZ3sBV8JAuy1", "PL6Gxj7bacnJg3TNk6FM61EdGa0QURH75c", "PLtJ9IXyRs0-h80DHLzmQZmyGXvJ3JVXD0", "PL7I-5ZCAvKL5AQzvDh4gRxiAiaLVuXG3k", "PLUd4QQK5_uzRDpNOAFvGEDbAPEmI8MoYi", "PLw3Mt4FUg5YTMF5sX2aAPhzPnWAexUVvn", "PLYfjOiz0A8zPRUTtyPLIbikyuhI3C_mJh"
        )),
        Kategoria("Paidika_Xena", "Παιδικά ξένα", 1, listOf(
            "PLTVRe9aECZ6eFMc7LUtXta79M-kq8hCM8", "PL3oW2tjiIxvS-4ZDcK0mqxvhxyXaA-qgg", "PLcpk5TCg3vzfFoNIsmxY92jQjTZN7Vlw3", "PLTVRe9aECZ6eB0le0FST321YWiGAyQGor", "PL028565C616627F50", "PLuGu7tm8J43tYvJDgBqSDtTYRkjtqx7kc", "RD6BH-Rxd-NBo", "PLrNZ_vsNya8Mkltry_4K0f30QfAJnbMmg", "PLBDSSmeWrStaomELhrftB8-gEbkuspqro", "PLn4s8r0gyEeuMMAB5xeLatLpb4oN1dRIA", "PLEPQby6_o7m34KVQslk3BJV-nWgBhD-mk", "PLdkj6XH8GYPQOAwPQ34FRa7_Oa49MbRcZ"
        )),
    )

    val synolo: Int get() = oles.sumOf { it.listes.size }
}
