package com.donation.ddb.Controller;

import com.donation.ddb.Converter.CampaignCommentLikeConverter;
import com.donation.ddb.Converter.CampaignConverter;
import com.donation.ddb.Converter.CampaignUpdateConverter;
import com.donation.ddb.Domain.*;
import com.donation.ddb.Dto.Request.*;
import com.donation.ddb.Dto.Response.CampaignResponse;
import com.donation.ddb.Dto.Response.OrganizationResponse;
import com.donation.ddb.ImageStore;
import com.donation.ddb.Repository.projection.CampaignWithUpdate;
import com.donation.ddb.Service.CampaignCommentLikeService.CampaignCommentLikeService;
import com.donation.ddb.Service.CampaignCommentQueryService.CampaignCommentQueryService;
import com.donation.ddb.Service.CampaignPlansService.CampaignPlanCommandService;
import com.donation.ddb.Service.CampaignPlansService.CampaignPlansQueryService;
import com.donation.ddb.Service.CampaignService.CampaignCommandService;
import com.donation.ddb.Service.CampaignService.CampaignQueryService;
import com.donation.ddb.Service.CampaignSpendingService.CampaignSpendingCommandService;
import com.donation.ddb.Service.CampaignSpendingService.CampaignSpendingQueryService;
import com.donation.ddb.Service.CampaignUpdateService.CampaignUpdateCommandService;
import com.donation.ddb.Service.DonationService.DonationService;
import com.donation.ddb.Service.OrganizationUserService.OrganizationUserQueryService;
import com.donation.ddb.apiPayload.ApiResponse;
import com.donation.ddb.apiPayload.code.status.ErrorStatus;
import com.donation.ddb.apiPayload.exception.handler.CampaignHandler;
import com.donation.ddb.validation.ExistCampaign;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@NoArgsConstructor
@RequestMapping("/api/v1/campaigns")
@Slf4j
@Validated
public class CampaignController {
    @Autowired
    private CampaignQueryService campaignService;

    @Autowired
    private OrganizationUserQueryService organizationUserQueryService;

    @Autowired
    private CampaignPlansQueryService campaignPlansQueryService;

    @Autowired
    private CampaignSpendingQueryService campaignSpendingQueryService;

    @Autowired
    private CampaignCommentQueryService campaignCommentQueryService;

    @Autowired
    private CampaignCommentQueryService campaignCommentService;

    @Autowired
    private CampaignCommentLikeService campaignCommentLikeService;
    @Autowired
    private CampaignPlanCommandService campaignPlanCommandService;
    @Autowired
    private CampaignUpdateCommandService campaignUpdateCommandService;
    @Autowired
    private CampaignSpendingCommandService campaignSpendingCommandService;
    @Autowired
    private CampaignCommandService campaignCommandService;

    @Autowired
    private DonationService donationService;


    @GetMapping("home")
    public ApiResponse<?> campaignList() {
        List<CampaignResponse.CampaignListDto> popular = campaignService.findAllCampaigns(
                null,
                null,
                "FUNDRAISING",
                "POPULAR"
        );
        List<CampaignResponse.CampaignListDto> latest = campaignService.findAllCampaigns(
                null,
                null,
                "FUNDRAISING",
                "LATEST"
        );
        List<CampaignResponse.CampaignListDto> endingSoon = campaignService.findAllCampaigns(
                null,
                null,
                "FUNDRAISING",
                "ENDING_SOON"
        );
        List<CampaignWithUpdate> recentUpdates = campaignService.findRecentUpdates();

        // 🔥 총 기부금 조회 추가
        BigDecimal totalDonation = donationService.findAllAmount();

        Map<String, Object> campaignResponseDtoList = Map.of(
                "popular", popular,
                "latest", latest,
                "endingSoon", endingSoon,
                "recentUpdates", CampaignConverter.toRecentUpdateListDto(recentUpdates),
                "totalDonation", totalDonation  // 🔥 총 기부금 추가
        );
        return ApiResponse.onSuccess(campaignResponseDtoList);
    }

    // 진행 중 캠페인 리스트
    @GetMapping("fundraising")
    public ResponseEntity<?> inProgressCampaignList(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "sortType", required = false) String sortType
    ) {

        List<CampaignResponse.CampaignListDto> campaignResponseDtoList = campaignService.findAllCampaigns(
                null,
                category,
                "FUNDRAISING",
                sortType
        );

        Map<String, Object> resMap = Map.of(
                "campaigns", campaignResponseDtoList,
                "totalElements", campaignResponseDtoList.size()
        );

        return ResponseEntity.ok(resMap);
    }

    // 완료된 캠페인 리스트
    @GetMapping("completed")
    public ResponseEntity<?> endedCampaignList(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "sortType", required = false) String sortType
    ) {

        List<CampaignResponse.CampaignListDto> campaignResponseDtoList = campaignService.findAllCampaigns(
                null,
                category,
                "COMPLETED",
                sortType
        );

        Map<String, Object> resMap = Map.of(
                "campaigns", campaignResponseDtoList,
                "totalElements", campaignResponseDtoList.size()
        );

        return ResponseEntity.ok(resMap);
    }

    // 검색
    @GetMapping("search")
    public ResponseEntity<?> campaignList(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "statusFlag", required = false) String statusFlag,
            @RequestParam(value = "sortType", required = false) String sortType
    ) {
        // CampaignCategory로 변환
        if (category == null || category.isEmpty()) {
            category = "ALL"; // 기본값 설정
        }

        // CampaignSortType으로 변환

        List<CampaignResponse.CampaignListDto> campaignResponseDtoList = campaignService.findAllCampaigns(
                keyword,
                category,
                statusFlag,
                sortType
        );

        Map<String, Object> resMap = Map.of(
                "campaigns", campaignResponseDtoList,
                "totalElements", campaignResponseDtoList.size()
        );

        return ResponseEntity.ok(resMap);
    }

    @PostMapping(value = "")
    public ApiResponse<?> addCampaign(
            @RequestPart(value="request", name="request") @Valid CampaignRequestDto.JoinDto request,
            @RequestPart(value = "image") MultipartFile image,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) throws IOException {
        String email = null;

        if (userDetails == null) {
            throw new CampaignHandler(ErrorStatus._UNAUTHORIZED);
        } else if (!userDetails.isOrganization()) {
            throw new CampaignHandler(ErrorStatus._FORBIDDEN);
        } else {
            email = userDetails.getUsername();
        }

        request.setImageUrl("default.png");
        Campaign campaign = campaignService.addCampaign(request, email);

        String imagePath = ImageStore.storeImage(image, "\\campaigns\\" + campaign.getCId() + "\\detail\\");
        campaign.setCImageUrl(imagePath);

        campaign = campaignService.updateCampaign(campaign);

        List<CampaignPlanRequestDto.JoinDto> campaignPlans = request.getPlans();

        campaignPlanCommandService.addCampaignPlan(campaignPlans, campaign);

        return ApiResponse.onSuccess(CampaignConverter.toJoinResult(campaign));
    }

    @GetMapping("{cId}")
    public ApiResponse<?> getCampaign(@PathVariable(value = "cId") Long cId) {
        Campaign campaign = campaignService.findBycId(cId);
        if (campaign == null) {
            throw new CampaignHandler(ErrorStatus.CAMPAIGN_NOT_FOUND);
        }

        OrganizationResponse.OrganizationDetailDto organizationDto = null;

        if (campaign.getOrganizationUser() != null) {
            organizationDto = organizationUserQueryService.convertToDetailDto(campaign.getOrganizationUser());
        }

        return ApiResponse.onSuccess(CampaignConverter.toCampaignDetailDto(
                campaign,
                organizationDto,
                campaignPlansQueryService.getCampaignPlanDetails(cId),
                campaignSpendingQueryService.getCampaignSpending(cId)
        ));
    }

    @GetMapping("{cId}/comments")
    public ApiResponse<?> getCampaignComments(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(value = "cId") Long cId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        String userEmail = null;

        if (userDetails != null && userDetails.isStudent()) {
            userEmail = userDetails.getUsername();
            System.out.println("User Email: " + userEmail);
        }

        if (page < 0) {
            throw new CampaignHandler(ErrorStatus.PAGE_NUMBER_INVALID);
        }
        if (size <= 0) {
            throw new CampaignHandler(ErrorStatus.PAGE_SIZE_INVALID);
        }

        Pageable pageable = PageRequest.of(page, size);

        return ApiResponse.onSuccess(campaignCommentQueryService.findCommentByCampaignId(cId, pageable, userEmail));
    }

    @PostMapping("{cId}/comments")
    public ApiResponse<?> addCampaignComment(
            @PathVariable(value = "cId") Long cId,
            @RequestBody CampaignCommentRequestDto campaignCommentRequestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        // 캠페인 댓글 추가 로직
        if (userDetails == null) {
            throw new CampaignHandler(ErrorStatus._UNAUTHORIZED);
        }

        if (!userDetails.isStudent()) {
            throw new CampaignHandler(ErrorStatus._FORBIDDEN);
        }

        log.info("userDetails: {}", userDetails);
        log.info("email: {}", userDetails.getUsername());

        String email = userDetails.getUsername();

        CampaignComment newComment = campaignCommentService.addComment(
                campaignCommentRequestDto.getContent(),
                cId,
                email
        );

        return ApiResponse.onSuccess(
                Map.of(
                        "commentId", newComment.getCcId(),
                        "content", newComment.getCcContent(),
                        "createdAt", newComment.getCreatedAt()
                )
        );
    }

    @PostMapping("{cId}/comments/{ccId}/likes")
    public ApiResponse<?> addCommentLike(
            @PathVariable(value = "ccId") Long ccId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new CampaignHandler(ErrorStatus._UNAUTHORIZED);
        }

        if (!userDetails.isStudent()) {
            throw new CampaignHandler(ErrorStatus._FORBIDDEN);
        }

        String email = userDetails.getUsername();

        CampaignCommentLike campaignCommentLike = campaignCommentLikeService.toggleCommentLike(ccId, email);

        return ApiResponse.onSuccess(CampaignCommentLikeConverter.toDto(campaignCommentLike));

    }

    @PostMapping(value = "/{cId}/updates", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> addCampaignUpdate(
            @ExistCampaign @PathVariable(value = "cId") Long cId,
            @RequestPart(value="request", name="request") @Valid CampaignUpdateRequestDto.JoinDto request,
            @RequestPart(value = "image") MultipartFile image,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) throws IOException {
        Campaign campaign = campaignService.findBycId(cId);

        if (userDetails == null) {
            throw new CampaignHandler(ErrorStatus._UNAUTHORIZED);
        } else if (!userDetails.isOrganization() ||
                !campaign.getOrganizationUser().getOEmail().equals(userDetails.getUsername())) {
            log.info("User email: {}", userDetails.getUsername());
            log.info("Campaign organization email: {}", campaign.getOrganizationUser().getOEmail());
            throw new CampaignHandler(ErrorStatus._FORBIDDEN);
        }

        if (campaign.getCStatusFlag() != CampaignStatusFlag.COMPLETED) {
            throw new CampaignHandler(ErrorStatus.CAMPAIGN_NOT_COMPLETED);
        }

        String imagePath = ImageStore.storeImage(image, "\\campaigns\\" + cId + "\\updates\\");

        CampaignUpdate campaignUpdate = campaignUpdateCommandService.addCampaignUpdate(request, imagePath, cId);

        List<CampaignSpendingRequestDto.JoinDto> campaignSpendings = request.getSpendings();

        campaignSpendingCommandService.addCampaignPlan(campaignSpendings, campaign);

        return ApiResponse.onSuccess(CampaignUpdateConverter.toJoinResultDto(campaignUpdate));
    }

    @PatchMapping("/{cId}/status")
    public ApiResponse<?> updateCampaignStatus(
            @ExistCampaign @PathVariable(value = "cId") Long cId,
            @RequestBody @Valid CampaignRequestDto.UpdateStatusDto request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Campaign campaign = campaignService.findBycId(cId);

        if (userDetails == null) {
            throw new CampaignHandler(ErrorStatus._UNAUTHORIZED);
        } else if (!userDetails.isOrganization() ||
                !campaign.getOrganizationUser().getOEmail().equals(userDetails.getUsername())) {
            throw new CampaignHandler(ErrorStatus._FORBIDDEN);
        }

        campaignCommandService.updateStatusByUser(campaign, request.getStatus());

        return ApiResponse.onSuccess(CampaignConverter.toJoinResult(campaign));
    }
}