package com.retailer.oneops.agent.model;

import com.retailer.oneops.auth.model.MCategory;

import java.util.ArrayList;
import lombok.Data;

@Data

public class MAgentResponse {
    private String name,id,mobileNumber,imageUrl;
    private ArrayList<AgentImage> images;
    private MCategory category;
}
