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
    recoveryCode: string
    loading: boolean,
    message: string,
    finished: boolean
};

class ResetTwoFA extends Component<Props, State> {
    constructor(props: Props) {
        super(props);

        this.state = {
            recoveryCode: "",
            loading: false,
            message: "",
            finished: false
        };
    }

    validationSchema() {
        return Yup.object().shape({
            recoveryCode: Yup.string()
                .required("This field is required!"),
        });
    }

    handle2fa = (formValue: { recoveryCode: string }) => {
        const {recoveryCode} = formValue;
        const {navigate} = this.props;

        this.setState({
            message: "",
            loading: true
        });

        AuthService.resetTwoFA(recoveryCode.toString()).then(
            () => {
                this.setState({
                    finished: true
                });
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
            recoveryCode: "",
        };

        return (
            <div className="col-md-12">
                <div className="card card-container">
                    Reset Two Factor Authentication
                    {!this.state.finished &&
                        <Formik
                            initialValues={initialValues}
                            validationSchema={this.validationSchema}
                            onSubmit={this.handle2fa}
                        >
                            <Form>
                                <div className="form-group">
                                    <label htmlFor="recoveryCode">Enter one of your recovery codes:</label>
                                    <Field name="recoveryCode" type="text" className="form-control"
                                           pattern="[a-z0-9]{8}" minLength="8" maxLength="8" length="8"/>
                                    <ErrorMessage
                                        name="recoveryCode"
                                        component="div"
                                        className="alert alert-danger"
                                    />
                                </div>

                                <div className="form-group">
                                    <button type="submit" className="btn btn-primary btn-block" disabled={loading}>
                                        {loading && (
                                            <span className="spinner-border spinner-border-sm mr-1"></span>
                                        )}
                                        <span>Reset</span>
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
                    }
                    {this.state.finished &&
                        <div className="alert alert-success" role="alert">
                            Your Two Factor Authentication has been reset. <Link to={'/'}>Click here</Link> to continue.
                        </div>
                    }
                </div>
            </div>
        );
    }
}

export default withParamsAndNavigate(ResetTwoFA);
