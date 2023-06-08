## Price Server

### Just a small webserver I made to play around with async requests in spring boot with CI/CD in CircleCi.

[![CircleCI](https://dl.circleci.com/status-badge/img/gh/briandidthat/price-server/tree/development.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/gh/briandidthat/price-server/tree/development)

# Price Server
Cryptocurrency Spot Price API powered by Coinbase's free API

## Version: v1.0.5

### /spot

#### GET
##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| symbol | query |  | Yes | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /spot/statistics

#### GET
##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| symbol | query |  | Yes | string |
| startDate | query |  | Yes | date |
| endDate | query |  | No | date |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /spot/historical

#### GET
##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| symbol | query |  | Yes | string |
| date | query |  | Yes | date |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /spot/historical/batch

#### GET
##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| request | query |  | Yes | [BatchRequest](#BatchRequest) |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /spot/batch

#### GET
##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| request | query |  | Yes | [BatchRequest](#BatchRequest) |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |
