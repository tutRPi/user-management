import {Component} from "react";
import {Formik, Field, Form, ErrorMessage} from "formik";
import * as Yup from "yup";

import AuthService from "../../services/auth.service";
import {withParamsAndNavigate} from "../../common/withParamsAndNavigate";
import {Link} from "react-router-dom";

interface Props {
    navigate: any,
    props: any
}

type State = {
    twoFACode: string
    loading: boolean,
    message: string
};

class TwoFALogin extends Component<Props, State> {
    constructor(props: Props) {
        super(props);

        this.state = {
            twoFACode: "",
            loading: false,
            message: ""
        };
    }

    validationSchema() {
        return Yup.object().shape({
            twoFACode: Yup.string().required("This field is required!"),
        });
    }

    handle2fa = (formValue: { twoFACode: string }) => {
        const {twoFACode} = formValue;
        const {navigate} = this.props;

        this.setState({
            message: "",
            loading: true
        });

        AuthService.twoFA(twoFACode.toString()).then(
            () => {
                navigate("/profile");
                window.location.reload();
            },
            error => {
                const resMessage =
                    (error.response &&
                        error.response.data &&
                        error.response.data.message) ||
                    error.message ||
                    error.toString();

                this.setState({
                    loading: false,
                    message: resMessage
                });
            }
        );
    }

    render() {
        const {loading, message} = this.state;

        const initialValues = {
            twoFACode: "",
        };

        return (
            <div className="col-md-12">
                <div className="card card-container">
                    Two Factor Authentication Code
                    <Formik
                        initialValues={initialValues}
                        validationSchema={this.validationSchema}
                        onSubmit={this.handle2fa}
                    >
                        <Form>
                            <div className="form-group">
                                <label htmlFor="twoFACode">Please enter the code from your authenticator</label>
                                <Field name="twoFACode" type="text" className="form-control" inputMode="numeric"
                                       pattern="[0-9]{6}" minLength="6" maxLength="6" length="6"/>
                                <ErrorMessage
                                    name="twoFACode"
                                    component="div"
                                    className="alert alert-danger"
                                />
                            </div>

                            <div className="form-group">
                                <button type="submit" className="btn btn-primary btn-block" disabled={loading}>
                                    {loading && (
                                        <span className="spinner-border spinner-border-sm mr-1"></span>
                                    )}
                                    <span>Login</span>
                                </button>
                            </div>

                            {message && (
                                <div className="form-group">
                                    <div className="alert alert-danger" role="alert">
                                        {message}
                                    </div>
                                </div>
                            )}
                        </Form>
                    </Formik>
                    <div>
                        Click <Link to={'/reset2fa'}>here</Link>, if you lost your 2fa device
                    </div>
                </div>
            </div>
        );
    }
}

export default withParamsAndNavigate(TwoFALogin);
