package com.example.demo.Domain.Interfaces;

import java.util.UUID;

public interface TokensBlackList {
    public boolean getBlacksListedTokenbyId(String tokenId);
    public void addtBlackListedToken(String tokenId,long remainTimeForTokenToExpire);
}
