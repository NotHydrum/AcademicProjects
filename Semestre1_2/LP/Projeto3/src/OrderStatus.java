/**
 * The values of this type represent the possible outcomes for a validation of
 * an order description
 * @author LabP team
 *
 */
public enum OrderStatus {
    VALID_ORDER, 
    NO_BOX_START, 
    ORDER_VOLUME_EXCEEDS_BOX, 
    MISMATCHED_BOX_COVER, 
    NO_AVAILABLE_STOCK,
    UNCLOSED_BOX    
}
