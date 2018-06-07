# SpringWebSocketChat
Spring (MVC, WebSocket, Security), OAuth2 (github), Redis, SockJS

## Github OAuth2 app and Special Authorities

There is a great service called [Okta](https://www.okta.com/), that allows for giving authenticated users custom attributes that can be read by apps after successful authentication of the user.
One of uses of such custom attributes can be holding bonus authorities that are later mapped and stored in Spring Security user GrantedAuthority list.

Accessing custom fields is unfortunately impossible with **Github OAuth2**, since there is no option to attach custom attributes to users.
**Github OAuth2** gives every user *ROLE_USER* by default, and we have to design our solution ourselves if we want to have more than that.

That's why this app uses Redis to store custom authorities. For ex. owner of this app has *ROLE_ADMIN* stored with his username in that database.

When any user logs in using **Github OAuth2**, **OAuth2UserService**:
* creates a set of **GrantedAuthority** that is populated with authorities that are already bound to the user
* checks in Redis whether the user that currently wants to log in has any additional authorities, if this condition is true, the set holding authorities is updated
* recreates the **OAuth2User** object with updated authorities


