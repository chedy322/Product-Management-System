package com.example.demo.Domain.Interfaces;

import java.util.UUID;

public interface TokensBlackList {
    public boolean getBlacksListedTokenbyId(UUID tokenId);
    public void addtBlackListedToken(UUID tokenId);
}
