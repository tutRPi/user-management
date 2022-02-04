import React from "react";
import {useParams, useNavigate} from "react-router-dom";

export function withParamsAndNavigate(Component: typeof React.Component) {
    return (props: any) => (
        <Component {...props} params={useParams()} navigate={useNavigate()}/>
    );
}
